package br.com.matteusmoreno.garage_manager.ropository;

import br.com.matteusmoreno.garage_manager.domain.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public List<Product> findByNameContainingIgnoreCasePaginated(String[] keywords, int page, int pageSize) {
        StringBuilder queryBuilder = new StringBuilder();

        // Filtra apenas palavras com 3 ou mais caracteres
        List<String> validKeywords = Arrays.stream(keywords)
                .filter(word -> word.length() >= 3)  // Garante que tenha pelo menos 3 caracteres
                .toList();

        if (validKeywords.isEmpty()) {
            return List.of(); // Retorna lista vazia se nenhum critério válido for passado
        }

        // Constrói a query para múltiplas palavras
        for (int i = 0; i < validKeywords.size(); i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("LOWER(name) LIKE ?").append(i + 1);
        }

        return find(queryBuilder.toString(),
                validKeywords.stream()
                        .map(word -> "%" + word.toLowerCase() + "%")
                        .toArray(String[]::new))
                .page(page, pageSize)
                .list();
    }

}
