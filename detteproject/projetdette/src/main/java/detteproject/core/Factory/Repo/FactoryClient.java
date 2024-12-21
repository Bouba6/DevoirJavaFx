package detteproject.core.Factory.Repo;

// import detteproject.Repository.Bd.ClientRepositoryBD;
// import detteproject.Repository.Bd.UserRepositoryBD;
// import detteproject.Repository.List.ClientRepository;
import detteproject.Repository.jpa.RepositoryJpaClient;
import detteproject.Repository.jpa.RepositoryJpaUser;
import detteproject.core.RepositorieClient;
import detteproject.data.entities.Client;
import detteproject.services.ClientService;
// import detteproject.views.ClientView;
// import detteproject.views.UserView;

public class FactoryClient {

    private RepositorieClient repositorieClient;

    private ClientService clientService;

    // private ClientRepository clientRepository;

    // private ClientRepositoryBD clientRepositoryBD;

    // private UserRepositoryBD userRepositoryBD;

    private RepositoryJpaClient repositoryJpaClient;

    private RepositoryJpaUser userRepositoryJpa;

    public FactoryClient() {
        // userRepositoryBD = new UserRepositoryBD();
        userRepositoryJpa = new RepositoryJpaUser();
        // ClientRepositoryBD clientRepositoryBD = new
        // ClientRepositoryBD(userRepositoryBD);
        // ClientRepository clientRepository = new ClientRepository();
        this.repositorieClient = new RepositoryJpaClient(userRepositoryJpa);
        clientService = new ClientService(repositorieClient);
    }

    // public ClientRepository getClientRepository() {
    // return clientRepository;
    // }

    public ClientService getClientService() {
        return clientService;
    }

    public RepositorieClient getRepositorieClient() {
        return repositorieClient;
    }

    // public ClientRepositoryBD getClientRepositoryBD() {
    // return clientRepositoryBD;
    // }

}
