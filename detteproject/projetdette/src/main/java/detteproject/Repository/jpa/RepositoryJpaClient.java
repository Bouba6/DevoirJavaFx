package detteproject.Repository.jpa;

import detteproject.data.entities.Client;
import detteproject.data.entities.User;

import java.util.List;

import javax.persistence.Query;

import detteproject.core.RepositorieClient;
import detteproject.core.RepositoryJpaImpl;
import detteproject.core.Config.Repositorie;

public class RepositoryJpaClient extends RepositoryJpaImpl<Client> implements RepositorieClient {
    private RepositoryJpaUser repositoryJpaUser;

    public RepositoryJpaClient(RepositoryJpaUser repositoryJpaUser) {
        super(Client.class);
        this.repositoryJpaUser = repositoryJpaUser;
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean insert(Client objet) {
        try {
            if (objet.getUser() != null) {
                repositoryJpaUser.insert(objet.getUser());
                objet.setUser(repositoryJpaUser.getById(objet.getUser().getId()));
            }
            super.insert(objet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Client getBytel(String telephone) {
        try {
            Client result = em.createQuery("SELECT c FROM Client c WHERE c.telephone = :telephone", Client.class)
                    .setParameter("telephone", telephone)
                    .getSingleResult();
            if (result == null) {
                System.out.println("Client non existant");
                return null;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Client getByid(int id) {
        em.getTransaction().begin();
        try {
            Client client = em.find(Client.class, id);
            if (client == null) {
                System.out.println("Client non existant");
                return null;
            }
            return client;
        } finally {
            em.getTransaction().commit();
        }
    }

    @Override
    public Client getClientByUser(User user) {
        em.getTransaction().begin();
        try {
            Client client = em.createQuery("SELECT c FROM Client c WHERE c.user = :user", Client.class)
                    .setParameter("user", user)
                    .getSingleResult();
            if (client == null) {
                System.out.println("Client non existant");
                return null;
            }
            return client;
        } finally {
            em.getTransaction().commit();
        }
    }

}