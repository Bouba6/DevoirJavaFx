package detteproject.services;

import java.time.LocalDateTime;
import java.util.List;

import detteproject.State.EtatDette;
import detteproject.core.RepositorieDette;
import detteproject.core.ServiceDette;

import detteproject.core.Config.Repositorie;
import detteproject.core.Config.Service;
import detteproject.data.entities.Article;
import detteproject.data.entities.Client;
import detteproject.data.entities.DetailDette;
import detteproject.data.entities.Dette;
import detteproject.data.entities.Paiement;
import detteproject.data.entities.User;

public class DetteService implements ServiceDette {
    RepositorieDette detteRepository;

    public DetteService(RepositorieDette detteRepository) {
        this.detteRepository = detteRepository;
    }

    @Override
    public boolean save(Dette objet) {
        if (objet != null) {
            System.out.println(objet.getState().ordinal());
            objet.setCreateAt(LocalDateTime.now());
            // User user = UserConnected.getUserConnected();
            // objet.setUserCreate(user);
            objet.setMontantRestant(objet.getMontant());
            detteRepository.insert(objet);
            return true;
        }
        return false;
    }

    @Override
    public void update(Dette objet) {
        if (objet != null) {
            objet.setUpdateAt(LocalDateTime.now());

            detteRepository.update(objet);
        }
    }

    @Override
    public List<Dette> show() {
        return detteRepository.selectAll();
    }

    @Override
    public Dette getById(int id, Client client) {
        return detteRepository.getById(id, client);
    }

    @Override
    public List<Dette> ListDetEc(Client client) {
        return detteRepository.ListDetEc(client);
    }

    @Override
    public List<DetailDette> findArtInDet(Dette dette) {
        return detteRepository.ListDetArt(dette);
    }

    @Override
    public List<Paiement> ListDetPai(Dette dette) {
        return detteRepository.ListDetPai(dette);
    }

    @Override
    public List<Dette> ListDetByEtat(EtatDette etat) {
        return detteRepository.showByEtat(etat);
    }

    @Override
    public Dette getById1(int id) {
        return detteRepository.getById1(id);
    }

    @Override
    public boolean save1(Dette objet) {
        objet.setCreateAt(LocalDateTime.now());

        return detteRepository.insert1(objet);
    }

}
