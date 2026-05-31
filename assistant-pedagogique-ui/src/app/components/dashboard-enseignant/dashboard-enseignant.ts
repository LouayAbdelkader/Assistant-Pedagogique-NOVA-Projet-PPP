import {
  Component,
  OnInit,
  OnDestroy,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { HttpClient } from '@angular/common/http';

import { FormsModule } from '@angular/forms';

import {
  Router,
  RouterLink,
  NavigationEnd
} from '@angular/router';

import {
  filter,
  Subscription,
  forkJoin
} from 'rxjs';

import Chart from 'chart.js/auto';

@Component({
  selector: 'app-dashboard-enseignant',
  standalone: true,

  imports: [
    CommonModule,
    RouterLink,
    FormsModule
  ],

  templateUrl: './dashboard-enseignant.html',

  styleUrls: ['./dashboard-enseignant.css']
})

export class DashboardEnseignant
implements OnInit, OnDestroy {

  // =========================
  // DATA
  // =========================

  historique: any[] = [];

  etudiants: any[] = [];

  questionsParEtudiant: any[] = [];

  sessionsParEtudiant: any[] = [];

  nomEnseignant: string = '';

  questionsAujourdHui = 0;

  topQuestions: any[] = [];
  showNewPassword = false;
  showConfirmPassword = false;

  // =========================
  // CHARTS
  // =========================

  questionsChart: any;

  sessionsChart: any;

  topQuestionsChart: any;
  topQuestionsUserChart: any;
  topQuestionsUser: any[] = [];

selectedStudentId: number | null = null;

  // =========================
  // STATS
  // =========================

  stats = {

    totalQuestions: 0,

    etudiantsActifs: 0
  };

  // =========================
  // MODAL PROFIL
  // =========================

  showProfileModal = false;

  editName = '';

  newPassword = '';

  confirmPassword = '';

  // =========================
  // LOADING
  // =========================

  isLoading = false;

  // =========================
  // SUBSCRIPTION
  // =========================

  private routerSubscription!: Subscription;

  constructor(

    private http: HttpClient,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) {}

  // =========================
  // INIT
  // =========================

  ngOnInit(): void {

    this.nomEnseignant =

      localStorage.getItem('userName')

      || 'Enseignant';

    this.loadData();

    this.routerSubscription =

      this.router.events

        .pipe(

          filter(

            event =>

              event instanceof NavigationEnd &&

              this.router.url === '/dashboard'
          )
        )

        .subscribe(() => {

          this.loadData();
        });
  }

  // =========================
  // DESTROY
  // =========================

  ngOnDestroy(): void {

    if (this.routerSubscription) {

      this.routerSubscription.unsubscribe();
    }
  }

  loadTopQuestionsByUser(
  userId: number
): void {

  this.selectedStudentId = userId;

  this.http.get<any[]>(

    `http://localhost:8080/api/dashboard/top-questions/user/${userId}`

  ).subscribe({

    next: (data) => {

      this.topQuestionsUser = data || [];

      console.log(
        'Top Questions User',
        this.topQuestionsUser
      );

      setTimeout(() => {

        this.createTopQuestionsUserChart();

      }, 100);

    },

    error: (err) => {

      console.error(err);
    }
  });
}


// =========================
// TOP QUESTIONS CHART
// =========================

createTopQuestionsChart(): void {

  const labels =

    this.topQuestions.map(

      item => item.question
    );

  const data =

    this.topQuestions.map(

      item => item.count
    );

  if (this.topQuestionsChart) {

    this.topQuestionsChart.destroy();
  }

  this.topQuestionsChart = new Chart(

    'topQuestionsChart',

    {

      type: 'bar',

      data: {

        labels,

        datasets: [

          {

            data,

            borderRadius: 12,

            borderWidth: 0,

            backgroundColor: [

              '#185FA5',
              '#2A7CC7',
              '#3B9AE1',
              '#5DB5F5',
              '#87CDFD'
            ]
          }
        ]
      },

      options: {

        indexAxis: 'y',

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

          legend: {

            display: false
          }
        },

        scales: {

          x: {

            beginAtZero: true,

            ticks: {

              stepSize: 1,

              precision: 0
            }
          },

          y: {

            grid: {

              display: false
            }
          }
        }
      }
    }
  );
}


createTopQuestionsUserChart(): void {

  const labels =

    this.topQuestionsUser.map(
      q => q.question
    );

  const data =

    this.topQuestionsUser.map(
      q => q.count
    );

  if (this.topQuestionsUserChart) {

    this.topQuestionsUserChart.destroy();
  }

  this.topQuestionsUserChart = new Chart(

    'topQuestionsUserChart',

    {

      type: 'doughnut',

      data: {

        labels,

        datasets: [

          {

            data,

            backgroundColor: [

              '#185FA5',
              '#2A7CC7',
              '#3B9AE1',
              '#5DB5F5',
              '#87CDFD'
            ]
          }
        ]
      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

          legend: {

            position: 'right'
          }
        }
      }
    }
  );
}

  // =========================
  // MODAL METHODS
  // =========================

  openProfileModal(): void {

    this.editName = this.nomEnseignant;

    this.showProfileModal = true;
  }

  closeProfileModal(): void {

    this.showProfileModal = false;

    this.newPassword = '';

    this.confirmPassword = '';
  }

  saveProfile(): void {

    if (

      this.newPassword !==

      this.confirmPassword

    ) {

      alert(
        'Les mots de passe ne correspondent pas'
      );

      return;
    }

    // Mise à jour locale

    this.nomEnseignant = this.editName;

    localStorage.setItem(

      'userName',

      this.editName
    );

    console.log(
      'Nom :',
      this.editName
    );

    console.log(
      'Mot de passe :',
      this.newPassword
    );

    // TODO API backend

    this.closeProfileModal();
  }

  // =========================
  // QUESTIONS CHART
  // =========================

  createQuestionsChart(): void {

    const labels =

      this.questionsParEtudiant.map(

        item => item.nomEtudiant
      );

    const data =

      this.questionsParEtudiant.map(

        item => item.nombreQuestions
      );

    if (this.questionsChart) {

      this.questionsChart.destroy();
    }

    this.questionsChart = new Chart(

      'questionsChart',

      {

        type: 'bar',

        data: {

          labels: labels,

          datasets: [

            {

              label: 'Questions',

              data: data,

              borderWidth: 1,

              borderRadius: 10,

              barPercentage: 0.7,

              categoryPercentage: 0.8,

              backgroundColor: '#3BC7E3',

              borderColor: 'transparent'
            }
          ]
        },

        options: {

          responsive: true,

          maintainAspectRatio: false,

          layout: {

            padding: 20
          },

          plugins: {

            legend: {

              display: false
            }
          },

          scales: {

            y: {

              beginAtZero: true,

              ticks: {

                stepSize: 1,

                precision: 0
              }
            }
          }
        }
      }
    );
  }

  // =========================
  // SESSIONS CHART
  // =========================

  createSessionsChart(): void {

    const labels =

      this.sessionsParEtudiant.map(

        item => item.nomEtudiant
      );

    const data =

      this.sessionsParEtudiant.map(

        item => item.nombreSessions
      );

    if (this.sessionsChart) {

      this.sessionsChart.destroy();
    }

    this.sessionsChart = new Chart(

      'sessionsChart',

      {

        type: 'bar',

        data: {

          labels: labels,

          datasets: [

            {

              label: 'Sessions',

              data: data,

              borderWidth: 1,

              borderRadius: 10,

              barPercentage: 0.7,

              categoryPercentage: 0.8,

              backgroundColor: '#A4D100',

              borderColor: 'transparent'
            }
          ]
        },

        options: {

          responsive: true,

          maintainAspectRatio: false,

          layout: {

            padding: 20
          },

          plugins: {

            legend: {

              display: false
            }
          },

          scales: {

            y: {

              beginAtZero: true,

              ticks: {

                stepSize: 1,

                precision: 0
              }
            }
          }
        }
      }
    );
  }

  // =========================
  // LOAD DATA
  // =========================

  loadData(): void {

    this.isLoading = true;

    forkJoin({

      historique: this.http.get<any[]>(

        'http://localhost:8080/api/assistant/historique'
      ),

      etudiants: this.http.get<any[]>(

        'http://localhost:8080/api/users/etudiants'
      ),

      questionsParEtudiant: this.http.get<any[]>(

        'http://localhost:8080/api/dashboard/questions-par-etudiant'
      ),

      questionsAujourdHui: this.http.get<number>(

        'http://localhost:8080/api/dashboard/questions-aujourdhui'
      ),

      sessionsParEtudiant: this.http.get<any[]>(

        'http://localhost:8080/api/dashboard/sessions-par-etudiant'
      ),
     topQuestions: this.http.get<any[]>(

'http://localhost:8080/api/dashboard/top-questions'

),

    }).subscribe({

      next: (response) => {

        console.log(
          '📚 Historique :',
          response.historique
        );

        console.log(
          '👥 Étudiants :',
          response.etudiants
        );

        console.log(
          '📊 Questions :',
          response.questionsParEtudiant
        );

        console.log(
          '🧠 Sessions :',
          response.sessionsParEtudiant
        );

        // =========================
        // HISTORIQUE
        // =========================

        this.historique =

          (response.historique || [])

            .filter(

              (inter: any) =>

                inter.roleEtudiant ===
                'ETUDIANT'
            );

        // =========================
        // ÉTUDIANTS
        // =========================

        this.etudiants =

          response.etudiants || [];

        // =========================
        // QUESTIONS
        // =========================

        this.questionsParEtudiant =

          (response.questionsParEtudiant || [])

            .filter(

              (item: any) =>

                item.role === 'ETUDIANT'
            );

        // =========================
        // SESSIONS
        // =========================

        this.sessionsParEtudiant =

          (response.sessionsParEtudiant || [])

            .filter(

              (item: any) =>

                item.role === 'ETUDIANT'
            );

        // =========================
        // KPI
        // =========================

        this.questionsAujourdHui =

          response.questionsAujourdHui || 0;

         this.topQuestions = response.topQuestions || []; 

        this.calculerStats();

        this.isLoading = false;

        this.cdr.detectChanges();

        // =========================
        // CHARTS
        // =========================

        setTimeout(() => {

          if (

            this.questionsParEtudiant.length > 0

          ) {

            this.createQuestionsChart();
          }

          if (

            this.sessionsParEtudiant.length > 0

          ) {

            this.createSessionsChart();
          }

        }, 100);

        if (this.topQuestions.length > 0) {

  this.createTopQuestionsChart();
}
      },

      error: (err) => {

        console.error(
          '❌ ERREUR DASHBOARD'
        );

        console.error(err);

        this.isLoading = false;

        this.cdr.detectChanges();
      }
    });

    
  }

  // =========================
  // STATS
  // =========================

  calculerStats(): void {

    this.stats.totalQuestions =

      this.historique.length;

    const emails = new Set<string>();

    this.historique.forEach(inter => {

      if (inter.emailEtudiant) {

        emails.add(inter.emailEtudiant);
      }
    });

    this.stats.etudiantsActifs =

      emails.size;
  }

  // =========================
  // LOGOUT
  // =========================

  logout(): void {

    localStorage.clear();

    this.router.navigate(['/login']);
  }
}