package detteproject.core.Factory.Impl;

import detteproject.core.ServiceYml;
import detteproject.core.Config.Repositorie;
import detteproject.core.Factory.FactoryRepositoryInterface;
import detteproject.data.entities.Article;
import detteproject.data.entities.Client;
import detteproject.data.entities.Dette;
import detteproject.data.entities.Paiement;
import detteproject.data.entities.User;

import java.util.Map;

import detteproject.Repository.Bd.UserRepositoryBD;
import detteproject.Repository.jpa.RepositoryJpaClient;
import detteproject.Repository.jpa.RepositoryJpaUser;

public class FactoryRepo<T> implements FactoryRepositoryInterface<T> {
    private final Repositorie<T> repositorie;

    @SuppressWarnings("unchecked")
    public FactoryRepo(Class<T> clazz) {
        ServiceYml serviceYml = new ServiceYml();
        Map<String, Object> config = serviceYml.readYml("META-INF/config.yaml");

        if (Client.class.isAssignableFrom(clazz)) {
            String yh = (String) ((Map<String, Object>) config.get("repositories")).get("clientRepository");
            String yh2 = (String) ((Map<String, Object>) config.get("repositories")).get("userRepository");
            this.repositorie = (Repositorie<T>) CreateRepositoryInstance(yh, yh2, null,
                    null);
            // this.repositorie = (Repositorie<T>) CreateRepositoryInstance(yh, null,
            // null);
        }

        // else if (User.class.isAssignableFrom(clazz)) {
        // String yh = (String) ((Map<String, Object>)
        // config.get("repositories")).get("userRepository");
        // this.repositorie = (Repositorie<T>) CreateRepositoryInstance(yh, null, null,
        // null);

        // // this.repositorie = (Repositorie<T>) CreateRepositoryInstance(yh, null,
        // null);

        // }

        else if (Dette.class.isAssignableFrom(clazz)) {
            String yh3 = (String) ((Map<String, Object>) config.get("repositories")).get("detteRepository");
            String yh4 = (String) ((Map<String, Object>) config.get("repositories")).get("clientRepository");
            String yh5 = (String) ((Map<String, Object>) config.get("repositories")).get("articleRepository");
            String yh6 = (String) ((Map<String, Object>) config.get("repositories")).get("detaildetteRepository");
            this.repositorie = (Repositorie<T>) (Object) CreateRepositoryInstance(yh3,
                    yh4, yh5, yh6);
            // this.repositorie = (Repositorie<T>) (Object) CreateRepositoryInstance(yh3,
            // null, null);
        } else if (Article.class.isAssignableFrom(clazz)) {
            String yh = (String) ((Map<String, Object>) config.get("repositories")).get("articleRepository");
            this.repositorie = (Repositorie<T>) (Object) CreateRepositoryInstance(yh, null, null, null);

        }

        // else if (Paiement.class.isAssignableFrom(clazz)) {
        // String yh = (String) ((Map<String, Object>)
        // config.get("repositories")).get("paiementRepository");
        // String yh2 = (String) ((Map<String, Object>)
        // config.get("repositories")).get("detteRepository");
        // this.repositorie = (Repositorie<T>) (Object) CreateRepositoryInstance(yh,
        // yh2, null, null);
        // // this.repositorie = (Repositorie<T>) (Object) CreateRepositoryInstance(yh,
        // // null, null);

        // }

        else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz.getName());
        }
    }

    @Override
    public Repositorie<T> createRepository() {
        return repositorie;
    }

    private Object CreateRepositoryInstance(String classname, String classname2, String classname3, String classname4) {
        try {
            Class<?> clazz = Class.forName(classname);

            for (var constructor : clazz.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    // No-arg constructor
                    return constructor.newInstance();
                } else if (constructor.getParameterCount() == 1 && classname2 != null) {
                    // Single-parameter constructor
                    Class<?> clazz2 = Class.forName(classname2);
                    Object paramInstance = clazz2.getDeclaredConstructor().newInstance();
                    return constructor.newInstance(paramInstance);
                } else if (constructor.getParameterCount() == 2 && classname2 != null && classname3 != null) {
                    // Two-parameter constructor

                    // Two-parameter constructor
                    Class<?> clazz2 = Class.forName(classname2);
                    Class<?> clazz3 = Class.forName(classname3);
                    UserRepositoryBD userRepositoryBD = new UserRepositoryBD();

                    Object paramInstance1 = clazz2.getConstructor(UserRepositoryBD.class).newInstance(userRepositoryBD);

                    Object paramInstance2 = clazz3.getDeclaredConstructor().newInstance();
                    return constructor.newInstance(paramInstance1, paramInstance2);
                } else if (constructor.getParameterCount() == 3 && classname2 != null && classname3 != null
                        && classname4 != null) {

                    Class<?> clazz2 = Class.forName(classname2);
                    Class<?> clazz3 = Class.forName(classname3);
                    Object paramInstance1;

                    // if (clazz2.equals(ClientRepositoryBD.class)) {
                    // UserRepositoryBD userRepositoryBD = new UserRepositoryBD();
                    // paramInstance1 = clazz2.getConstructor(UserRepositoryBD.class)
                    // .newInstance(userRepositoryBD);
                    // }

                    if (clazz2.equals(RepositoryJpaClient.class)) {
                        RepositoryJpaUser repositoryJpaUser = new RepositoryJpaUser();
                        paramInstance1 = clazz2.getConstructor(RepositoryJpaUser.class)
                                .newInstance(repositoryJpaUser);
                    }

                    else {
                        throw new RuntimeException("Unsupported class: " + clazz2.getName());
                    }

                    Object paramInstance2 = clazz3.getDeclaredConstructor().newInstance();
                    Class<?> clazz4 = Class.forName(classname4);
                    Object paramInstance3 = clazz4.getDeclaredConstructor().newInstance();
                    return constructor.newInstance(paramInstance1, paramInstance2, paramInstance3);

                }
            }
            System.err.println("No suitable constructor found for class: " + classname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
