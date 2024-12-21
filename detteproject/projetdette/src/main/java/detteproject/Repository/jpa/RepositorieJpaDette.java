package detteproject.Repository.jpa;

import java.time.LocalDateTime;
import java.util.List;

import detteproject.State.EtatDette;

import detteproject.core.RepositorieDette;
import detteproject.core.RepositoryJpaImpl;

import detteproject.data.entities.Article;
import detteproject.data.entities.Client;
import detteproject.data.entities.DetailDette;
import detteproject.data.entities.Dette;
import detteproject.data.entities.Paiement;

public class RepositorieJpaDette extends RepositoryJpaImpl<Dette> implements RepositorieDette {
    private RepositoryJpaClient repositoryJpaClient;
    private RepositorieJpaArticle repositorieJpaArticle;
    private RepositorieJpaDetail repositoryJpaDetail;

    public RepositorieJpaDette(RepositoryJpaClient repositoryJpaClient, RepositorieJpaArticle repositorieJpaArticle,
            RepositorieJpaDetail repositoryJpaDetail) {
        super(Dette.class);
        this.repositoryJpaClient = repositoryJpaClient;
        this.repositorieJpaArticle = repositorieJpaArticle;
        this.repositoryJpaDetail = repositoryJpaDetail;

    }

    public RepositorieJpaDette() {
        super(Dette.class);
    }

    public boolean insert(Dette objet) {
        try {
            // Call the superclass insert method, which handles the transaction for Dette
            super.insert(objet);

            // Handle inserting the details of the Dette entity
            List<DetailDette> details = objet.getDetails();
            for (DetailDette detail : details) {
                detail.onPrePersist();
                detail.setUserCreate(objet.getUserCreate());
                repositoryJpaDetail.insert(detail);
                detail.getArticle().setQteStock(detail.getArticle().getQteStock() - detail.getQte());
                Article article = detail.getArticle();
                article.onPrePersist();
                article.setUserCreate(objet.getUserCreate());
                repositorieJpaArticle.update(article); // Updates the Article entity
            }

            // Update the associated Client entity
            Client client = objet.getClient();
            client.setUserUpdate(objet.getUserCreate());
            client.setUpdateAt(LocalDateTime.now());
            repositoryJpaClient.update(client);

            // Since transaction is managed in the super.insert(), no need for another
            // commit here
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean insert1(Dette objet) {
        try {
            // Call the superclass insert method, which handles the transaction for Dette
            super.insert(objet);

            // Handle inserting the details of the Dette entity
            List<DetailDette> details = objet.getDetails();
            for (DetailDette detail : details) {
                detail.onPrePersist();
                detail.setUserCreate(objet.getUserCreate());
                repositoryJpaDetail.insert(detail);
                Article article = detail.getArticle();
                article.onPrePersist();
                article.setUserCreate(objet.getUserCreate());
                repositorieJpaArticle.update(article); // Updates the Article entity
            }

            // Update the associated Client entity
            Client client = objet.getClient();
            client.setUserUpdate(objet.getUserCreate());
            client.setUpdateAt(LocalDateTime.now());
            repositoryJpaClient.update(client);

            // Since transaction is managed in the super.insert(), no need for another
            // commit here
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Dette getById(int id, Client client) {
        em.getTransaction().begin();
        try {
            Dette commande = em.find(Dette.class, id);
            if (commande == null) {
                System.out.println("commande non existante");
                return null;
            } else {
                return commande;
            }
        } finally {
            em.getTransaction().commit();
        }

    }

    private String getTableName() {
        return "commande";
    }

    @Override
    public List<Dette> ListDetEc(Client client) {
        em.getTransaction().begin();
        try {
            String sql = String.format("SELECT * FROM %s WHERE clientid = ? ", getTableName());
            List<Dette> list = (List<Dette>) em.createNativeQuery(sql, Dette.class)
                    .setParameter(1, client.getId())
                    .getResultList();
            return list;
        } finally {
            em.getTransaction().commit();
        }
    }

    @Override
    public List<DetailDette> ListDetArt(Dette commande) {
        em.getTransaction().begin();
        try {
            String sql = "SELECT * FROM detailDette Where detteid = ?";
            List<DetailDette> list = em.createNativeQuery(sql, DetailDette.class)
                    .setParameter(1, commande.getId())
                    .getResultList();
            return list;
        } finally {
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Paiement> ListDetPai(Dette commande) {
        em.getTransaction().begin();
        try {
            String sql = "SELECT * FROM paiement WHERE detteid = ?";
            List<Paiement> list = em.createNativeQuery(sql, Paiement.class).setParameter(1, commande.getId())
                    .getResultList();
            return list;
        } finally {
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Dette> showByEtat(EtatDette etat) {
        em.getTransaction().begin();
        try {
            String sql = String.format("SELECT * FROM %s WHERE etatid = ?", getTableName());
            List<Dette> list = em.createNativeQuery(sql, Dette.class).setParameter(1, etat.ordinal())
                    .getResultList();
            return list;
        } finally {
            em.getTransaction().commit();
        }
    }

    @Override
    public Dette getById1(int id) {
        em.getTransaction().begin();
        try {
            Dette commande = em.find(Dette.class, id);
            if (commande == null) {
                System.out.println("Commande non existante");
                return null;
            } else {
                return commande;
            }
        } finally {
            em.getTransaction().commit();
        }
    }

}
