package detteproject.services;

import java.time.LocalDateTime;
import java.util.List;

import detteproject.core.RepositorieArticle;
import detteproject.core.ServiceArticle;

import detteproject.core.Config.Repositorie;
import detteproject.core.Config.Service;
import detteproject.data.entities.Article;
import detteproject.data.entities.User;

public class ArticleService implements ServiceArticle {
    RepositorieArticle articleRepository;

    public ArticleService(RepositorieArticle articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public boolean save(Article objet) {
        if (objet != null) {
            objet.setCreateAt(LocalDateTime.now());

            articleRepository.insert(objet);
            return true;
        }
        return false;
    }

    @Override
    public void update(Article objet) {
        objet.setUpdateAt(LocalDateTime.now());

        articleRepository.update(objet);
    }

    @Override
    public List<Article> show() {
        return articleRepository.selectAll();
    }

    @Override
    public Article findById(int id) {
        return articleRepository.getById(id);

    }

}