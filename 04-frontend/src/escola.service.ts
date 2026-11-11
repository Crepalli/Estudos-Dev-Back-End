import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpInterceptorFn } from '@angular/common/http';
import { Aluno, AlunoInput, Cep, Pagina, Token } from './models';

@Injectable({ providedIn: 'root' }) // Uma instância compartilhada enquanto a aplicação estiver aberta.
export class EscolaService {
  private http = inject(HttpClient);
  token = ''; // Fica somente em memória; recarregar a página exige novo login.

  login(usuario: string, senha: string) { return this.http.post<Token>('/auth/login', { usuario, senha }); }
  listar(pagina: number) { return this.http.get<Pagina>(`/api/alunos?pagina=${pagina}&tamanho=10`); }
  salvar(aluno: AlunoInput, id?: number) {
    return id === undefined ? this.http.post<Aluno>('/api/alunos', aluno)
      : this.http.put<Aluno>(`/api/alunos/${id}`, aluno); // A presença do ID distingue criação de edição.
  }
  excluir(id: number) { return this.http.delete<void>(`/api/alunos/${id}`); }
  buscarCep(cep: string) { return this.http.get<Cep>(`/api/ceps/${cep}`); }
}

// Centraliza o envio do token e restringe o cabeçalho às URLs relativas da nossa API.
export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const service = inject(EscolaService);
  if (service.token && req.url.startsWith('/api/')) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${service.token}` } });
  }
  return next(req); // Continua a requisição, modificada ou original.
};
