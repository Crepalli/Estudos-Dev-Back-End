// Interfaces verificam o formato dos dados durante a compilação do TypeScript.
export interface Endereco { cep: string; logradouro: string; numero: string; cidade: string; uf: string; }
export interface AlunoInput { matricula: string; nome: string; email: string; endereco: Endereco; }
export interface Aluno extends AlunoInput { id: number; }
export interface Pagina { content: Aluno[]; totalPages: number; totalElements: number; }
export interface Token { accessToken: string; tokenType: string; expiresIn: number; }
export interface Cep { cep: string; logradouro: string; localidade: string; uf: string; }
