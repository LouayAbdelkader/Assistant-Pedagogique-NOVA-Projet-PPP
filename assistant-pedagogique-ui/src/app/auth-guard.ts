import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  // Si le token existe, on laisse passer
  if (token) {
    return true;
  }

  // Sinon, on bloque et on redirige vers le login
  router.navigate(['/login']);
  return false;
};