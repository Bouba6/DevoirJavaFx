package detteproject.services;

import java.time.LocalDateTime;
import java.util.List;

import detteproject.State.Etat;

// import detteproject.Repository.List.ClientRepository;
import detteproject.State.Etat;
import detteproject.core.RepositorieClient;
import detteproject.core.ServiceClient;

import detteproject.core.Config.Repositorie;
import detteproject.core.Config.Service;
import detteproject.data.entities.Client;
import detteproject.data.entities.User;

public class ClientService implements ServiceClient {
    RepositorieClient clientRepository;

    public ClientService(RepositorieClient clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean save(Client objet) {
        if (objet != null) {
            objet.setCreateAt(LocalDateTime.now());
            boolean result = clientRepository.insert(objet);
            if (result) {
                System.out.println("Enregistrement reussi");
                return true;
            } else {
                System.out.println("Enregistrement echoué");
                return false;
            }
        }
        return false;

    }

    @Override
    public Client find(String tel) {
        return clientRepository.getBytel(tel);
    }

    @Override
    public void update(Client objet) {
        if (objet != null) {
            objet.setUpdateAt(LocalDateTime.now());
            clientRepository.update(objet);
        }
        return;
    }

    @Override
    public List<Client> show() {
        return clientRepository.selectAll();

    }

    @Override
    public Client findById(int id) {
        return clientRepository.getByid(id);
    }

    @Override
    public Client findClientByUser(User user) {
        return clientRepository.getClientByUser(user);
    }

}
