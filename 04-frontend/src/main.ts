import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { App } from './app';
import { tokenInterceptor } from './escola.service';

// bootstrap inicia o componente raiz; providers disponibiliza o cliente HTTP e o interceptor.
bootstrapApplication(App, { providers: [provideHttpClient(withInterceptors([tokenInterceptor]))] })
  .catch(erro => console.error('Não foi possível iniciar a aplicação.', erro));
