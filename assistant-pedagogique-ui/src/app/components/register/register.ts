import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

/**
 * Composant d'inscription (Register)
 * Permet la création de nouveaux comptes (étudiants ou enseignants)
 * 
 * Fonctionnalités :
 * - Formulaire complet avec validation
 * - Sélection du rôle (Étudiant / Enseignant)
 * - Gestion des erreurs avec messages utilisateur
 * - Redirection vers login après succès
 */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  registerForm: FormGroup;  // Formulaire d'inscription
  showPassword = false;      // Affichage mot de passe
  isLoading = false;         // Indicateur de chargement
  errorMessage = '';         // Message d'erreur à afficher

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // Initialisation du formulaire avec validations
    this.registerForm = this.fb.group({
      nom: ['', Validators.required],                                    // Nom requis
      email: ['', [Validators.required, Validators.email]],              // Email valide
      password: ['', [Validators.required, Validators.minLength(6)]],   // Minimum 6 caractères
      role: ['ETUDIANT']                                                  // Rôle par défaut
    });
  }

  /**
   * Soumission du formulaire d'inscription
   */
  onSubmit(): void {
    // Validation côté client
    if (this.registerForm.invalid) {
      this.errorMessage = 'Veuillez remplir correctement tous les champs.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Construction du payload attendu par le backend Spring Boot
    const payload = {
      nom: this.registerForm.value.nom,
      email: this.registerForm.value.email,
      motDePasse: this.registerForm.value.password,  // Note : "motDePasse" pour Spring Boot
      role: this.registerForm.value.role
    };

    this.authService.register(payload).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/login']); // Redirection vers connexion
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Erreur inscription :', err);
        
        // Extraction intelligente du message d'erreur
        if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else if (typeof err.error === 'string') {
          this.errorMessage = err.error;
        } else if (err.message) {
          this.errorMessage = err.message;
        } else {
          this.errorMessage = 'Erreur lors de l’inscription. Réessayez.';
        }
      }
    });
  }

  /**
   * Affiche ou masque le mot de passe
   */
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}