import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { RouterLink } from '@angular/router';

/**
 * Composant de connexion (Login)
 * Gère l'authentification des utilisateurs
 * 
 * Fonctionnalités :
 * - Formulaire réactif avec validation
 * - Affichage/masquage du mot de passe
 * - Redirection dynamique selon le rôle (étudiant → chat, enseignant → dashboard)
 */
@Component({
  selector: 'app-login',
  standalone: true, // Composant standalone (pas besoin de NgModule)
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  loginForm: FormGroup;  // Formulaire réactif
  showPassword = false;   // État d'affichage du mot de passe

  constructor(
    private fb: FormBuilder,        // Création de formulaires réactifs
    private authService: AuthService, // Service d'authentification
    private router: Router          // Service de navigation
  ) {
    // Initialisation du formulaire avec validations
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],   // Email requis et valide
      password: ['', [Validators.required]]                   // Mot de passe requis
    });
  }

  /**
   * Soumission du formulaire de connexion
   * Envoie les identifiants au backend et gère la réponse
   */
  onSubmit() {
    if (this.loginForm.valid) {
      // Formatage du payload attendu par le backend Spring Boot
      const payload = {
        email: this.loginForm.value.email,
        motDePasse: this.loginForm.value.password
      };
      
      // Appel au service d'authentification
      this.authService.login(payload).subscribe({
        next: (res) => {
          // Sauvegarde du token et des infos utilisateur
          this.authService.saveUser(res);
          
          // Redirection selon le rôle
          const role = this.authService.getRole();
          if (role === 'ENSEIGNANT') {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/chat']);
          }
        },
        error: () => alert("Identifiants incorrects")
      });
    }
  }

  /**
   * Affiche ou masque le mot de passe
   * Alterne entre input type="password" et type="text"
   */
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}