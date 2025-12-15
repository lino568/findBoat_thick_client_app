package fr.cda.findboat.helper;

public class InfoContent {

    private InfoContent() {
    }

    public static final String INFO_CONTENT = """
                MANUEL UTILISATEUR - FIND'BOAT
                
                
                1. PRÉSENTATION
                
                Find'Boat est une application permettant de rechercher des bateaux à louer sur différents sites de location.
                L'objectif est de simplifier la recherche en interrogeant plusieurs sites simultanément et en centralisant les résultats.
                
                
                2. EFFECTUER UNE RECHERCHE
                
                Dans la partie basse de l'écran, les champs suivants sont disponibles :
                
                - Date de début : sélectionner la date de début de location souhaitée
                - Date de fin : sélectionner la date de fin de location souhaitée
                - Type de bateaux : choisir le type de bateau recherché (voilier, catamaran, etc.)
                - Ville : saisir le nom de la ville (l'autocomplétion s'active à partir de 3 caractères)
                
                Sélectionner ensuite au moins un des sites suivants :
                - Crouesty location
                - Atlantique location
                - LocVoileArmor
                
                Une fois tous les champs remplis, cliquer sur le bouton "Rechercher".
                
                Note : la recherche peut prendre quelques instants. Une barre de progression indique l'avancement du traitement.
                
                
                3. RÉSULTATS
                
                Les bateaux trouvés s'affichent dans le tableau situé en haut de l'écran avec les informations suivantes :
                - Photo
                - Titre
                - Description
                - Capacité (nombre de personnes)
                - Longueur
                - Année du bateau
                
                Il est possible de cliquer sur une ligne pour obtenir plus de détails.
                
                
                4. ENREGISTRER LES RÉSULTATS
                
                Après une recherche, plusieurs options sont disponibles dans le menu "Fichier" :
                
                - Enregistrer dans un fichier : génère un PDF contenant tous les bateaux trouvés
                - Envoi Couriel : permet d'envoyer les résultats par email
                - Enregistrer dans la base de données : sauvegarde les bateaux dans la base de données
                
                
                5. PARAMÈTRES
                
                Le menu "Paramètres" > "Base de données" permet de configurer la connexion à la base de données MySQL pour la sauvegarde des données.
                
                
                6. BOUTON EFFACER
                
                Le bouton "Effacer" permet de réinitialiser tous les champs de recherche pour effectuer une nouvelle recherche.
                
                
                7. NOTES IMPORTANTES
                
                - Une connexion internet est nécessaire pour le fonctionnement de l'application
                - La date de fin doit être postérieure à la date de début
                - La ville doit être valide (utiliser l'autocomplétion pour s'assurer de la validité)
                - Au moins un site de location doit être sélectionné
                - En cas d'erreur, un message s'affiche en rouge en bas de l'écran
                
                """;
}
