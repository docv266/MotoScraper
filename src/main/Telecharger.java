package main;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Telecharger
{

	public static void main(String[] args) throws IOException
	{

		File dossier = new File("save");
		File sortie = new File("sortie.csv");

		if (sortie.exists())
		{
			sortie.delete();
		}

		// Créer le dossier s'il n'existe pas
		if (!dossier.exists())
		{
			dossier.mkdirs();
		}

		// Parcours du document racine (listant les années)
		Document docRoot = Jsoup.connect("http://www.bikez.com/years/index.php").get();

		// On récupère les éléments faisant partie de l'élément ayant pour classe "zebra", de type "a", avec un attribut "href" et contenant exactement le texte "list view"
		Elements table = docRoot.select(".zebra a[href]:containsOwn(list view)");

		for (Element linkAnnee : table)
		{

			// Parcours du document listant les modèle pour une année donnée.
			Document docAnnee = Jsoup.connect(linkAnnee.attr("abs:href")).get();

			// On récupère les éléments faisant partie de l'élément ayant pour classe "zebra", de type "a", avec un attribut "href".
			// Puis on retire les éléments "a" qui contiennent un élément "img" (On retire ainsi les lien vers les annonces contenant une image, toutes les annonces n'ont pas nécessairement une image)
			// Puis on retire les éléments "a" qui ont un attribut "href" commençant par "/brand/" (On retire ainsi les lien vers la marque)
			Elements modele = docAnnee.select(".zebra a[href]").not("a:has(img)").not("a[href^=/brand/]");

			for (Element linkModele : modele)
			{
				System.out.println(linkModele.attr("abs:href"));
			}

			break;

		}
	}

}
