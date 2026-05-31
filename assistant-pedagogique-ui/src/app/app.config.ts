import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { routes } from './app.routes';

/**
 * Configuration globale de l'application Angular
 * Alternative moderne aux NgModules pour les applications standalone
 */
export const appConfig: ApplicationConfig = {
  providers: [
    // Configuration du routeur avec toutes les routes définies
    provideRouter(routes),
    
    // Configuration du client HTTP avec support des intercepteurs
    provideHttpClient(withInterceptorsFromDi()),
    
    // Enregistrement de l'intercepteur JWT pour ajouter automatiquement le token
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ]
};