package br.com.fourshopp.service;

import br.com.fourshopp.entities.Produto;
import br.com.fourshopp.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto create(Produto operador){
        return produtoRepository.save(operador);
    }

    public Produto findById(Long id){
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Objeto não encontrado"));
    }

    public List<Produto> listAll(){
        return produtoRepository.findAll();
    }

    public void remove(Long id){
        produtoRepository.deleteById(id);
    }

    public Produto update(Produto produto, Long id){
        Produto found = findById(id);
        found.setDataVencimento(produto.getDataVencimento());
        found.setNome(produto.getNome());
        found.setQuantidade(produto.getQuantidade());
        found.setPreco(produto.getPreco());
        return produtoRepository.save(found);
    }

    public List<Produto> listaProdutosPorSetor(int setor){
        return produtoRepository.findBySetor(setor);
    }

    public void diminuirEstoque(int quantidade, Produto produto){
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        produtoRepository.save(produto);
    }

    public String remover(Scanner scanner){
        System.out.println("Escolha o produto que será excluído pelo Id:");

        List<Produto> produtos = listAll();

        for (Produto produto: produtos){
            System.out.println(produto.getNome() + " Id: " + produto.getId());
        }

        int id;
        Produto produto;
        try {
            id = Integer.parseInt(scanner.next());
            produto = findById((long) id);
            produto.getNome();
        } catch (Exception e){
            return "Id inválido!";
        }

        produtoRepository.deleteById(produto.getId());
        return "Produto removido com sucesso!";
    }
}
