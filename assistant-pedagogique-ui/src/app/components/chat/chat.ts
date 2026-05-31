import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ChatService } from '../../services/chat';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './chat.html',
  styleUrls: ['./chat.css']
})
export class ChatComponent implements OnInit {
  sessions: any[] = [];
  activeSession: any = null;
  interactions: any[] = [];
  newMessage = '';
  isTyping = false;
  isLoading = true;  // ← indicateur de chargement

  isEnseignant = false;
  userName = '';
  showProfileModal = false;
  editName = '';
  newPassword = '';
  confirmPassword = '';

  showNewPassword = false;
showConfirmPassword = false;
  constructor(
    private chatService: ChatService,
    private router: Router,
    private cdr: ChangeDetectorRef   // ← force la mise à jour de l'affichage
  ) {}

  ngOnInit() {
    this.isEnseignant = localStorage.getItem('role') === 'ENSEIGNANT';
    this.userName = localStorage.getItem('userName') || 'Utilisateur';
    this.loadSessions();
  }

  loadSessions() {
    this.isLoading = true;
    this.cdr.detectChanges();
    console.log('Chargement des sessions...');
    
    this.chatService.getSessions().subscribe({
      next: (data) => {
        console.log('Sessions reçues :', data);
        this.sessions = data;
        this.isLoading = false;
        this.cdr.detectChanges();  // ← force l'affichage

        if (this.sessions.length > 0 && !this.activeSession) {
          this.selectSession(this.sessions[0]);
        }
      },
      error: (err) => {
        console.error('Erreur détaillée :', err);
        this.isLoading = false;
        this.cdr.detectChanges();
        if (err.status === 401) {
          alert('Session expirée, veuillez vous reconnecter.');
          this.router.navigate(['/login']);
        } else {
          alert('Erreur de chargement des discussions. Voir console.');
        }
      }
    });
  }

  createNewSession() {
    this.chatService.createSession().subscribe({
      next: (session) => {
        console.log('Session créée :', session);
        this.sessions = [session, ...this.sessions];
        this.activeSession = session;
        this.interactions = [];
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erreur création session', err)
    });
  }

  selectSession(session: any) {
    if (this.activeSession?.id === session.id) return;
    this.activeSession = session;
    this.interactions = [];
    this.cdr.detectChanges();
    
    this.chatService.getInteractions(session.id).subscribe({
      next: (data) => {
        this.interactions = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erreur chargement interactions', err)
    });
  }

  sendMessage() {
    if (!this.newMessage.trim() || !this.activeSession || this.isTyping) return;
    const questionText = this.newMessage.trim();
    this.newMessage = '';
    
    this.interactions.push({
  question: questionText,
  reponse: null,
  section: null,
  chapitre: null,
  categorie: null,
  slideId: null,
  timestamp: new Date().toISOString()
});
    this.isTyping = true;
    this.cdr.detectChanges();

    this.chatService.askQuestion(this.activeSession.id, questionText).subscribe({
      next: (res) => {
        const index = this.interactions.length - 1;
        this.interactions[index] = res;
        this.isTyping = false;
        
        if (this.interactions.length === 1 && res.session?.nomSession) {
          this.activeSession.nomSession = res.session.nomSession;
          const idx = this.sessions.findIndex(s => s.id === this.activeSession.id);
          if (idx !== -1) this.sessions[idx].nomSession = res.session.nomSession;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isTyping = false;
        this.interactions.pop();
        this.cdr.detectChanges();
        console.error(err);
        alert('Erreur de communication avec l’assistant.');
      }
    });
  }
 openProfileModal() {

  this.editName = this.userName;

  this.newPassword = '';

  this.confirmPassword = '';

  this.showProfileModal = true;
}

closeProfileModal() {

  this.showProfileModal = false;
}

saveProfile() {

  if (
    this.newPassword &&
    this.newPassword !== this.confirmPassword
  ) {

    alert('Les mots de passe ne correspondent pas');

    return;
  }

  const payload: any = {

    nom: this.editName
  };

  if (this.newPassword.trim()) {

    payload.motDePasse = this.newPassword;
  }

  this.chatService.updateProfile(payload)
    .subscribe({

      next: (res) => {

        localStorage.setItem(
          'userName',
          res.nom
        );

        this.userName = res.nom;

        this.showProfileModal = false;

        this.cdr.detectChanges();

        alert('Profil mis à jour avec succès');
      },

      error: (err) => {

        console.error(err);

        alert('Erreur lors de la mise à jour');
      }
    });
}
  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

 

}