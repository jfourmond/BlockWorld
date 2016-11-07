# BlockWorld

Travail pratique de Système Multi-Agents sous la direction de :
- [Dr. HASSAS Salima](https://sites.google.com/site/mysitesalima/),

Par : 
- [FOURMOND Jérôme](https://github.com/jfourmond/)

Dans le cadre de l'Unité d'Enseignement **Système Multi-Agents** du [Master 2 Informatique - Parcours Intelligence Artificielle](http://master-info.univ-lyon1.fr/IA/) de l'[Université Claude Bernard Lyon 1](http://www.univ-lyon1.fr/).

Langage :
- Java

---

# Objectif

Le but du travail pratique est de modéliser le monde des blocs : un jeu qui consiste à faire une pile de bloc dans un ordre précis à partir d'un état initial, avec à disponibilité trois emplacements ; à l'aide d'agents.

## Etat Initial

| 1 | 2 | 3 |
|---|---|---|
| a |   |   |
| c |   |   |
| b |   |   |
| d |   |   |

## Etat Final

| 1 | 2 | 3 |
|---|---|---|
| a |   |   |
| b |   |   |
| c |   |   |
| d |   |   |

L'emplacement final de la pile n'est pas arbitraire.

## Les Agents
Les **agents** ont connaissance de leurs buts et perçoivent plusieurs entités :
- le bloc du dessous
- leur liberté (s'il y a un bloc au-dessus)
- les emplacements libres

Et un comportement comportant différentes actions :
- Mouvement : SI (NON satisfait ET libre) ALORS Mouvement();
- Pousse : SI (NON satisfait ET NON free) ALORS Pousse();

## Perception
Les **perceptions** s'effectuent à partir de demande à l'environnement.

# Compilation & Exécution

Le répertoire dispose d'un fichier **Ant** pour la compilation et l'exécution du programme en ligne de commande.

### Compilation

Pour compiler, uniquement, le programme :

	ant build

### Exécution

Pour exécuter le programme :

	ant run