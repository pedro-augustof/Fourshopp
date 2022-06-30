package br.com.fourshopp;

import br.com.fourshopp.Util.UtilMenu;
import br.com.fourshopp.entities.*;
import br.com.fourshopp.enums.Cargo;
import br.com.fourshopp.enums.Setor;
import br.com.fourshopp.repository.OperadorRespository;
import br.com.fourshopp.service.ClienteService;
import br.com.fourshopp.service.FuncionarioService;
import br.com.fourshopp.service.OperadorService;
import br.com.fourshopp.service.ProdutoService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.fourshopp.Util.UtilMenu.*;

@SpringBootApplication
public class FourShoppApplication implements CommandLineRunner {

    Scanner scanner = new Scanner(System.in);

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FuncionarioService funcionarioService;


    private Cliente cliente;

    public static void main(String[] args) {
        SpringApplication.run(FourShoppApplication.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {

        while (true) {
            System.out.println("====== BEM-VINDO AO FOURSHOPP ======");
            System.out.println("1- Sou cliente \n2- Área do ADM \n3- Seja um Cliente \n4- Login funcionário \n5- Encerrar ");
            int opcao = 5;
            try {
                opcao = Integer.parseInt(scanner.next());
                menuInicial(opcao);
            } catch (NumberFormatException e) {
                System.err.println("Opção inválida!");
            }
        }
    }

    public void menuInicial(int opcao) throws CloneNotSupportedException, IOException, ParseException {
        if (opcao == 1) {
            System.out.println("Insira seu cpf: ");
            String cpf = scanner.next();
            System.out.println("Insira sua senha: ");
            String password = scanner.next();
            this.cliente = clienteService.loadByEmailAndPassword(cpf, password).orElseThrow(() -> new ObjectNotFoundException(1L, "Cliente"));
            if (cliente == null) {
                System.err.println("Usuario não encontrado !");
                menuInicial(4);
            }

            int contador = 1;
            while (contador == 1) {
                System.out.println("Escolha o setor: ");
                int setor = menuSetor(scanner);


                List<Produto> collect = produtoService.listaProdutosPorSetor(setor).stream().filter(x -> x.getSetor() == setor).collect(Collectors.toList());
                collect.forEach(produto -> {
                    System.out.println(produto.getId() + "- " + produto.getNome() + " Preço: " + produto.getPreco() + " Estoque - " + produto.getQuantidade());
                });

                Produto foundById;
                Long produto;
                while (true) {
                    System.out.println("Informe o número do produto desejado: ");

                    try {
                        produto = Long.parseLong(scanner.next());
                        foundById = produtoService.findById(produto);
                        foundById.getNome();
                        break;
                    } catch (Exception e) {
                        System.err.println("Número do produto inválido!");
                    }
                }

                int quantidade;
                while (true) {
                    System.out.println("Escolha a quantidade");

                    try {
                        quantidade = Integer.parseInt(scanner.next());

                        if (foundById.getQuantidade() < quantidade){
                            System.err.println("Quantidade inválida!");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Quantidade inválida!");
                    }
                }

                // Atualiza estoque

                produtoService.diminuirEstoque(quantidade, foundById);

                Produto clone = foundById.clone();
                System.out.println(clone.toString());
                clone.getCalculaValor(quantidade, clone);
                cliente.getProdutoList().add(clone);
                System.out.println("Deseja outro produto S/N ?");
                String escolha = scanner.next();

                if (!escolha.equalsIgnoreCase("S")) {
                    contador = 2;
                    gerarCupomFiscal(cliente);
                    System.out.println("Gerando nota fiscal...");
                    System.err.println("Fechando a aplicação...");
                }
            }
        }

        if (opcao == 2) {
            System.out.println("INTRANET FOURSHOPP....");

            System.out.println("Insira as credenciais do usuário administrador: ");

            System.out.println("Insira seu cpf: ");
            String cpf = scanner.next();

            System.out.println("Insira sua password: ");
            String password = scanner.next();

            Optional<Funcionario> admnistrador = this.funcionarioService.loadByEmailAndPassword(cpf, password);

            if (!admnistrador.isPresent()) {
                System.out.println("Administrador nao encontrado");
                return;
            }

            if (admnistrador.get().getCargo() != Cargo.ADMINISTRADOR) {
                System.out.println("Administrador nao encontrado");
                menuInicial(2);
            } else {
                int escolhaAdm;
                while (true) {
                    try {
                        System.out.println("1- Cadastrar funcionários \n2- Cadastrar Operador \n3 - Demitir funcionário");
                        escolhaAdm = Integer.parseInt(scanner.next());
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("Opção inválida!");
                    }
                }
                if (escolhaAdm == 1) {
                    Funcionario funcionario = cadastrarFuncionario(scanner);

                    if (funcionario != null) {
                        this.funcionarioService.create(funcionario);
                        System.out.println("Funcionário cadastrado com sucesso");
                    } else {
                        System.err.println("Cargo inválido!");
                    }
                } else if (escolhaAdm == 2) {
                    Operador operator = menuCadastrarOperador(scanner);
                    this.operadorService.create(operator);
                    System.out.println("Operador cadastrado com sucesso");
                } else if (escolhaAdm == 3) {
                    boolean loop = true;
                    while (loop) {
                        try {
                            System.out.println("1 - Demitir funcionário\n2 - Demitir operador");
                            int option = Integer.parseInt(scanner.next());

                            switch (option){
                                case 1:
                                    deleteFuncionario(scanner, funcionarioService);
                                    loop = false;
                                    break;
                                case 2:
                                    deleteOperador(scanner, operadorService);
                                    loop = false;
                                    break;
                                default:
                                    System.err.println("Opção inválida!");
                            }
                        } catch (NumberFormatException e){
                            System.err.println("Opção inválida!");
                        }
                    }
                } else
                    System.out.println("Opção inválida");

            }

        }

        if (opcao == 3) {
            Cliente cliente = menuCadastroCliente(scanner);
            this.clienteService.create(cliente);
            System.out.println("Bem-vindo, " + cliente.getNome());
            menuInicial(1);
        }

        if (opcao == 4) {
            System.out.println("Área do funcionário");
            int escolhaCargo;

            while (true) {
                try {
                    System.out.println("1- Chefe  \n2- Operador ");
                    escolhaCargo = Integer.parseInt(scanner.next());
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Escolha inválida!");
                }
            }

            System.out.println("Insira seu cpf: ");
            String cpf = scanner.next();

            System.out.println("Insira sua password: ");
            String password = scanner.next();

            if (escolhaCargo == 1) {
                Optional<Funcionario> worker = this.funcionarioService.loadByEmailAndPassword(cpf, password);
                if (!worker.isPresent()) {
                    System.err.println("Funcionário não encontrado!");
                    return;
                }

                if (worker.get().getCargo() != Cargo.CHEFE_SECAO) {
                    System.err.println("Você não tem permissão para acessar essa área!");
                    return;
                }

                Funcionario chefe = worker.get();
                boolean valid = true;
                while (valid) {
                    int option = 0;
                    try {
                        System.out.println("1 - Cadastrar produto");
                        System.out.println("2 - Cadastrar operadores");
                        System.out.println("3 - Excluir produto");
                        System.out.println("4 - Alterar estoque");
                        System.out.println("5 - Alterar preço");
                        option = Integer.parseInt(scanner.next());
                    } catch (NumberFormatException e) {
                        System.err.println("Opção inválida!");
                    }

                    valid = false;

                    switch (option) {
                        case 1:
                            Produto product = menuCadastrarProduto(scanner);
                            produtoService.create(product);
                            System.out.println("Produto criado com sucesso!");
                            break;
                        case 2:
                            Operador operator = menuCadastrarOperador(scanner);
                            operadorService.create(operator);
                            chefe.addOperator(operator);
                            funcionarioService.create(chefe);
                            System.out.println("Operador criado com sucesso!");
                            break;
                        case 3:
                            System.out.println(produtoService.remover(scanner));
                            break;
                        case 4:
                            alterarEstoque(scanner, produtoService);
                            break;
                        case 5:
                            alterarPreco(scanner, produtoService);
                            break;
                        default:
                            System.err.println("Opção inválida!");
                            valid = true;
                    }
                }
            } else {
                Optional<Operador> operador = this.operadorService.loadByEmailAndPassword(cpf, password);
            }
        }

        if (opcao == 5) {
            System.exit(0);
        }
    }


}
