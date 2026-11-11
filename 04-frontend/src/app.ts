import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs';
import { Aluno, AlunoInput } from './models';
import { EscolaService } from './escola.service';

@Component({ selector: 'app-root', standalone: true, imports: [FormsModule], templateUrl: './app.html' })
export class App {
  private api = inject(EscolaService); // O componente cuida da tela; o serviço conhece as URLs.
  usuario = 'estudante';
  senha = '';
  conectado = false;
  ocupado = false;
  mensagem = '';
  alunos: Aluno[] = [];
  pagina = 0;
  totalPaginas = 0;
  idEdicao?: number;
  dados: AlunoInput = this.vazio();

  private vazio(): AlunoInput {
    return { matricula: '', nome: '', email: '', endereco: { cep: '', logradouro: '', numero: '', cidade: '', uf: '' } };
  }

  entrar(form: NgForm) {
    if (form.invalid || this.ocupado) return;
    this.ocupado = true;
    this.mensagem = '';
    this.api.login(this.usuario, this.senha).pipe(finalize(() => this.ocupado = false)).subscribe({
      next: token => {
        this.api.token = token.accessToken; this.senha = ''; this.conectado = true;
        this.carregar();
      },
      error: erro => this.falhou(erro)
    });
  }

  sair() {
    this.api.token = ''; this.conectado = false; this.alunos = []; this.pagina = 0;
    this.idEdicao = undefined; this.dados = this.vazio(); // Limpa dados da sessão anterior.
  }

  carregar(pagina = this.pagina) {
    this.ocupado = true;
    this.api.listar(pagina).pipe(finalize(() => this.ocupado = false)).subscribe({
      next: resultado => {
        if (pagina > 0 && resultado.content.length === 0) { this.carregar(pagina - 1); return; }
        this.alunos = resultado.content; this.totalPaginas = resultado.totalPages; this.pagina = pagina;
      },
      error: erro => this.falhou(erro)
    });
  }

  editar(aluno: Aluno) {
    this.idEdicao = aluno.id;
    this.dados = { matricula: aluno.matricula, nome: aluno.nome, email: aluno.email, endereco: { ...aluno.endereco } };
    this.mensagem = ''; // Copia o endereço para não modificar a tabela antes de salvar.
  }

  cancelar(form: NgForm) {
    this.idEdicao = undefined; this.dados = this.vazio(); form.resetForm(this.dados);
  }

  salvar(form: NgForm) {
    if (form.invalid || this.ocupado) { form.control.markAllAsTouched(); return; }
    this.ocupado = true;
    this.api.salvar(this.dados, this.idEdicao).pipe(finalize(() => this.ocupado = false)).subscribe({
      next: () => { this.cancelar(form); this.mensagem = 'Aluno salvo.'; this.carregar(); },
      error: erro => this.falhou(erro)
    });
  }

  excluir(aluno: Aluno, form: NgForm) {
    if (this.ocupado || !confirm(`Excluir ${aluno.nome}?`)) return;
    this.ocupado = true;
    this.api.excluir(aluno.id).pipe(finalize(() => this.ocupado = false)).subscribe({
      next: () => {
        if (this.idEdicao === aluno.id) this.cancelar(form);
        this.mensagem = 'Aluno excluído.'; this.carregar();
      },
      error: erro => this.falhou(erro)
    });
  }

  consultarCep() {
    if (this.ocupado || !/^[0-9]{8}$/.test(this.dados.endereco.cep)) {
      this.mensagem = 'Informe oito dígitos no CEP.'; return;
    }
    this.ocupado = true;
    this.api.buscarCep(this.dados.endereco.cep).pipe(finalize(() => this.ocupado = false)).subscribe({
      next: endereco => {
        this.dados.endereco.logradouro = endereco.logradouro;
        this.dados.endereco.cidade = endereco.localidade;
        this.dados.endereco.uf = endereco.uf;
        this.mensagem = 'Endereço consultado. Confira os dados e informe o número.';
      },
      error: erro => this.falhou(erro) // Falha externa não impede preencher o endereço manualmente.
    });
  }

  private falhou(erro: HttpErrorResponse) {
    if (erro.status === 401) {
      this.sair(); this.mensagem = 'Credenciais inválidas ou sessão expirada. Entre novamente.';
    } else {
      this.mensagem = erro.error?.detail ?? (erro.status === 0 ? 'Não foi possível conectar à API.' : 'Não foi possível concluir a operação.');
    }
  }
}
