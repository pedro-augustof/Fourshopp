package br.com.fourshopp.Util;

import br.com.fourshopp.entities.*;
import br.com.fourshopp.enums.Cargo;
import br.com.fourshopp.enums.Setor;
import br.com.fourshopp.service.FuncionarioService;
import br.com.fourshopp.service.OperadorService;
import br.com.fourshopp.service.ProdutoService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class UtilMenu {

    private static double valorTotalCompra;

    // SOU CLIENTE
    public static Cliente menuCadastroCliente(Scanner scanner) throws ParseException {

        System.out.println("Insira seu nome: ");
        scanner.nextLine();
        String nome = scanner.nextLine();

        System.out.println("Insira seu email: ");
        String email = scanner.next();

        System.out.println("Insira seu celular: ");
        String celular = scanner.next();

        System.out.println("Insira sua password: ");
        String password = scanner.next();

        System.out.println("Insira seu cpf: ");
        String cpf = scanner.next();

        System.out.println("Insira sua rua: ");
        scanner.nextLine();
        String rua = scanner.nextLine();

        System.out.println("Insira seu cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("Insira seu bairro: ");
        String bairro = scanner.nextLine();

        int numero;
        String number;
        while (true) {
            try {
                System.out.print("Digite seu número: ");
                number = scanner.next();
                numero = Integer.parseInt(number);
                break;
            } catch (NumberFormatException e) {
                System.err.println("Digite apenas números!");
            }
        }

        Date data;
        while (true) {
            try {
                System.out.println("Data de nascimento: ");
                String hireDate = scanner.next();

                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                data = formato.parse(hireDate);
                break;

            } catch (ParseException e) {
                System.err.println("Data incompatível com o formato dd/MM/yyyy");
            }
        }

        Endereco endereco = new Endereco(rua, cidade, bairro, numero);
        Cliente cliente = new Cliente(nome, email, celular, password, cpf, endereco, data);

        return cliente;

    }


    public static int menuSetor(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Digite a opção desejada: " +
                        "\n1- MERCEARIA \n2- BAZAR \n3- ELETRÔNICOS");
                int opcao = Integer.parseInt(scanner.next());
                return opcao;
            } catch (NumberFormatException e) {
                System.err.println("Opção inválida");
            }
        }
    }

    public static void gerarCupomFiscal(Cliente cliente) throws IOException {
        List<Produto> produtos = cliente.getProdutoList();
        Document document = new Document(PageSize.A4);
        File file = new File("CupomFiscal_" + new Random().nextInt() + ".pdf");
        String absolutePath = file.getAbsolutePath();
        PdfWriter.getInstance(document, new FileOutputStream(absolutePath));
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Image image1 = Image.getInstance("src/main/java/br/com/fourshopp/service/fourshopp.png");
        image1.scaleAbsolute(140f, 140f);
        image1.setAlignment(Element.ALIGN_CENTER);


        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Font total = FontFactory.getFont(FontFactory.HELVETICA);
        total.setSize(12);
        total.setColor(Color.blue);

        Font header = FontFactory.getFont(FontFactory.HELVETICA);
        header.setSize(12);
        header.setFamily("bold");

        document.add(image1);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        ListItem item1 = new ListItem();
        produtos.forEach(produto -> {

            System.out.println(produto.toString());
            Chunk nome = new Chunk("\n" + produto.getNome() + " (" + produto.getQuantidade() + ") \nPreço unidade : R$" + df.format(produto.getPreco() / produto.getQuantidade()));
            Phrase frase = new Phrase();
            frase.add(nome);

            Paragraph x = new Paragraph(frase);

            String preco = "............................................................................................................................R$ " + df.format(produto.getPreco());
            Paragraph y = new Paragraph(preco);
            y.setAlignment(Paragraph.ALIGN_RIGHT);
            item1.add(x);
            item1.add(y);

            valorTotalCompra = valorTotalCompra + produto.getPreco();
        });

        if (valorTotalCompra >= 500){
            valorTotalCompra -= valorTotalCompra * 0.1;
        }

        Paragraph paragraph = new Paragraph("\n\nTOTAL: R$" + df.format(valorTotalCompra), total);
        paragraph.setAlignment(Paragraph.ALIGN_RIGHT);


        document.add(item1);
        document.add(paragraph);


        document.close();
    }

    public static Funcionario cadastrarFuncionario(Scanner scanner) throws ParseException {

        System.out.println("Insira seu nome: ");
        scanner.nextLine();
        String nome = scanner.nextLine();

        System.out.println("Insira seu email: ");
        String email = scanner.next();

        System.out.println("Insira seu celular: ");
        String celular = scanner.next();

        System.out.println("Insira sua password: ");
        String password = scanner.next();

        if (password.length() < 8) {
            System.err.println("Senha inválida!");
            return null;
        }

        System.out.println("Insira seu cpf: ");
        String cpf = scanner.next();

        if (!Validations.isCpf(cpf)) {
            System.err.println("Cpf inválido!");
            return null;
        }

        System.out.println("Insira sua rua: ");
        scanner.nextLine();
        String rua = scanner.nextLine();

        System.out.println("Insira seu cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("Insira seu bairro: ");
        String bairro = scanner.nextLine();

        int numero;
        String number;
        while (true) {
            try {
                System.out.print("Digite o número: ");
                number = scanner.next();
                numero = Integer.parseInt(number);
                break;
            } catch (NumberFormatException e) {
                System.err.println("Digite apenas números!");
            }
        }

        double salario;
        while (true) {
            try {
                System.out.println("Insira o salário CLT bruto: ");
                number = scanner.next();
                salario = Double.parseDouble(number);
                break;
            } catch (NumberFormatException e) {
                System.err.println("Digite apenas números!");
            }
        }

        Date data;
        while (true) {
            try {
                System.out.println("Data de contratação: ");
                String hireDate = scanner.next();

                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                data = formato.parse(hireDate);
                break;

            } catch (ParseException e) {
                System.err.println("Data incompatível com o formato dd/MM/yyyy");
            }
        }

        Cargo cargo;
        while (true) {
            try {
                System.out.println("Escolha o cargo: \n1 - ADMINISTRADOR,\n" +
                        "2 - CHEFE DE SEÇÃO,\n" +
                        "3 - GERENTE");

                cargo = Cargo.get(Integer.parseInt(scanner.next()));

                if (cargo == null) {
                    System.err.println("Entrada inválida!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida!");
            }
        }

        if (cargo == Cargo.OPERADOR) {
            return null;
        }

        Setor setor;
        while (true) {
            try {
                System.out.println("Escolha o setor: \n1 - MERCEARIA,\n" +
                        "2 - BAZAR,\n" +
                        "3 - ELETRONICOS, \n" +
                        "4 - COMERCIAL");

                setor = Setor.get(Integer.parseInt(scanner.next()));

                if (setor == null) {
                    System.err.println("Entrada inválida!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida!");
            }
        }

        Endereco endereco = new Endereco(rua, cidade, bairro, numero);
        return new Funcionario(nome, email, celular, password, cpf, endereco, data, cargo, setor, salario, new ArrayList<>(), new ArrayList<>());

    }

    public static Operador menuCadastrarOperador(Scanner scanner) throws ParseException {

        System.out.println("Insira seu nome: ");
        scanner.nextLine();
        String nome = scanner.nextLine();

        System.out.println("Insira seu email: ");
        String email = scanner.next();

        System.out.println("Insira seu celular: ");
        String celular = scanner.next();

        System.out.println("Insira sua password: ");
        String password = scanner.next();

        System.out.println("Insira seu cpf: ");
        String cpf = scanner.next();

        System.out.println("Insira sua rua: ");
        scanner.nextLine();
        String rua = scanner.nextLine();

        System.out.println("Insira seu cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("Insira seu bairro: ");
        String bairro = scanner.nextLine();

        int numero;
        while (true) {
            try {
                System.out.println("Insira seu numero: ");
                numero = Integer.parseInt(scanner.next());
                break;
            } catch (NumberFormatException e) {
                System.err.println("Digite apenas números!");
            }
        }

        Date data;
        while (true) {
            try {
                System.out.println("Data de contratação: ");
                String hireDate = scanner.next();

                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                data = formato.parse(hireDate);
                break;

            } catch (ParseException e) {
                System.err.println("Data incompatível com o formato dd/MM/yyyy");
            }
        }

        System.out.println("Insira o salário CLT bruto: ");
        scanner.nextLine();
        double salario = Double.parseDouble(scanner.nextLine());

        int carga;
        while (true){
            try {
                System.out.print("Defina uma carga horária: ");
                carga = Integer.parseInt(scanner.next());
                if (carga > 0){
                    break;
                } else {
                    System.err.println("Digite uma carga horária válida!");
                }

            }catch (NumberFormatException e){
                System.err.println("Digite uma carga horária válida!");
            }
        }

        return new Operador(nome, email, celular, password, cpf, new Endereco(rua, cidade, bairro, numero), data, Cargo.OPERADOR, salario, carga);
    }

    public static Produto menuCadastrarProduto(Scanner scanner) {
        System.out.print("Digite o nome do produto: ");
        scanner.nextLine();
        String name = scanner.nextLine();

        int quantity;
        while (true) {
            try {
                System.out.print("Digite a quantidade: ");
                quantity = Integer.parseInt(scanner.next());

                if (quantity > 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Digite uma quantidade válida!");
            }
        }

        double price;
        while (true) {
            try {
                System.out.print("Digite o preço: ");
                price = Double.parseDouble(scanner.next());

                if (price > 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Digite um preço válido!");
            }
        }

        Setor setor;
        while (true) {
            try {
                System.out.println("Escolha o setor: \n1 - MERCEARIA,\n" +
                        "2 - BAZAR,\n" +
                        "3 - ELETRONICOS, \n" +
                        "4 - COMERCIAL");

                setor = Setor.get(Integer.parseInt(scanner.next()));

                if (setor == null) {
                    System.err.println("Entrada inválida!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida!");
            }
        }

        Date data;
        while (true) {
            try {
                System.out.println("Data de vencimento: ");
                String hireDate = scanner.next();

                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                data = formato.parse(hireDate);
                break;

            } catch (ParseException e) {
                System.err.println("Data incompatível com o formato dd/MM/yyyy");
            }
        }

        return new Produto(name, quantity, price, setor, data);
    }

    public static void deleteFuncionario(Scanner scanner, FuncionarioService funcionarioService){
        List<Funcionario> employees = funcionarioService.listAll();

        System.out.println("Escolha um funcionário pelo CPF");

        for (Funcionario employee : employees){
            System.out.println(employee.getNome() + " CPF: " + employee.getCpf());
        }


        String cpf = scanner.next();

        System.out.println(funcionarioService.deleteByCpf(cpf));
    }

    public static void deleteOperador(Scanner scanner, OperadorService operadorService){
        List<Operador> operadores = operadorService.listAll();

        System.out.println("Escolha um operador pelo CPF");

        for (Operador operador : operadores){
            System.out.println(operador.getNome() + " CPF: " + operador.getCpf());
        }

        String cpf = scanner.next();

        System.out.println(operadorService.deleteByCpf(cpf));
    }

    public static void alterarEstoque(Scanner scanner, ProdutoService produtoService){
        System.out.println("Escolha o produto pelo Id:");

        List<Produto> produtos = produtoService.listAll();

        for (Produto produto: produtos){
            System.out.println(produto.getNome() + "Validade: " + produto.getDataVencimento() + " Id: " + produto.getId());
        }

        int id;
        Produto produto;
        try {
            id = Integer.parseInt(scanner.next());
            produto = produtoService.findById((long) id);
            produto.getNome();
        } catch (Exception e){
            System.err.println("Id inválido!");
            return;
        }

        int quantidade;
        while (true){
            try {
                System.out.print("Digite a quantidade que será acrescentada ao estoque: ");
                quantidade = Integer.parseInt(scanner.next());

                if (quantidade > 0){
                    break;
                } else {
                    System.err.println("Quantidade inválida!");
                }
            } catch (NumberFormatException e){
                System.err.println("Quantidade inválida!");
            }
        }

        produto.setQuantidade(produto.getQuantidade() + quantidade);
        produtoService.create(produto);

        System.out.println(quantidade + " unidades foram acrescentadas ao estoque");
    }

    public static void alterarPreco(Scanner scanner, ProdutoService produtoService){
        System.out.println("Escolha o produto pelo Id:");

        List<Produto> produtos = produtoService.listAll();

        for (Produto produto: produtos){
            System.out.println(produto.getNome() + "Validade: " + produto.getDataVencimento() + " Id: " + produto.getId());
        }

        int id;
        Produto produto;
        try {
            id = Integer.parseInt(scanner.next());
            produto = produtoService.findById((long) id);
            produto.getNome();
        } catch (Exception e){
            System.err.println("Id inválido!");
            return;
        }

        double preco;
        while (true){
            try{
                System.out.print("Digite o novo preço: ");
                preco = Double.parseDouble(scanner.next());

                if (preco > 0){
                    break;
                } else {
                    System.err.println("Digite um preço maior que 0!");
                }
            } catch (NumberFormatException e){
                System.err.println("Digite um valor válido!");
            }
        }

        produto.setPreco(preco);
        produtoService.create(produto);

        System.out.println("Preço alterado com sucesso!");
    }
}
