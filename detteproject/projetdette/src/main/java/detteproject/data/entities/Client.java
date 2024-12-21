package detteproject.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "telephone" })
})
public class Client extends AbstractEntity {

    @Column(length = 50)
    private String nom;

    @Column(name = "telephone", length = 50, unique = true, nullable = false)
    private String telephone;

    @Column(length = 50)
    private String adresse;

    @OneToMany(mappedBy = "client")
    private List<Dette> commandes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "\"userId\"")
    private User user;

    @Column(name = "solde")
    private Double solde;

    public void setDettes(Dette commande) {
        solde += commande.getMontant();
        commandes.add(commande);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + nom + '\'' +
                ", address='" + telephone + '\'' +
                ", montantDette='" + solde + '\'' +
                ", Telephone='" + telephone + '\'' +
                ", commandes=" + commandes + '}';
    }

}
