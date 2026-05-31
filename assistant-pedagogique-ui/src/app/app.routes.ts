import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { DashboardEnseignant } from './components/dashboard-enseignant/dashboard-enseignant';
import { ChatComponent } from './components/chat/chat';
import { authGuard } from './auth-guard';

export const routes: Routes = [
  // Redirection par défaut
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  
  // Routes publiques (non protégées)
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  
  // Routes privées (protégées par authGuard)
  { 
    path: 'dashboard', 
    component: DashboardEnseignant,
    canActivate: [authGuard] // <-- Bloque l'accès si non connecté
  },
  { 
    path: 'chat', 
    component: ChatComponent,
    canActivate: [authGuard] // <-- Bloque l'accès si non connecté
  },

  // Route 404 / Catch-all (À METTRE OBLIGATOIREMENT À LA TOUTE FIN)
  // Redirige toute URL inconnue (ex: /azerty) vers le login
  { path: '**', redirectTo: 'login' } 
];