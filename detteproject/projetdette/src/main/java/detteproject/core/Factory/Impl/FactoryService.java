package detteproject.core.Factory.Impl;

import detteproject.Repository.jpa.RepositoryJpaClient;
import detteproject.Repository.jpa.RepositoryJpaUser;
import detteproject.core.RepositorieArticle;
import detteproject.core.RepositorieClient;
import detteproject.core.RepositorieDette;
import detteproject.core.RepositoriePaiement;
import detteproject.core.RepositorieUser;
import detteproject.core.Config.Repositorie;
import detteproject.core.Config.Service;
import detteproject.core.Factory.FactoryServiceInterface;
import detteproject.data.entities.Article;
import detteproject.data.entities.Client;
import detteproject.data.entities.Dette;
import detteproject.data.entities.Paiement;
import detteproject.data.entities.User;
import detteproject.services.ArticleService;
import detteproject.services.ClientService;

import detteproject.services.DetteService;

public class FactoryService<T> implements FactoryServiceInterface<T> {
    private final Service<T> service;
    private final Repositorie<T> repositorie;

    @SuppressWarnings("unchecked")
    public FactoryService(Class<T> clazz, Repositorie<T> repositorie) {
        this.repositorie = repositorie;
        // isintanceof()
        if (Client.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new ClientService((RepositorieClient) repositorie); // Correct instantiation
        } else if (Dette.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new DetteService((RepositorieDette) repositorie); // Correct instantiation
        } else if (Article.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new ArticleService((RepositorieArticle) repositorie); // Correct instantiation
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz.getName());
        }
    }

    @Override
    public Service<T> createService() {
        return service; // Return the created service
    }
}
