package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

		BufferedWriter writer = new BufferedWriter(new FileWriter(sortie));

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
				Moto maMoto = new Moto();

				// Parcours du document détaillant le modèle
				Document docMoto = Jsoup.connect(linkModele.attr("abs:href")).get();

				maMoto.setMarque(docMoto.select("h1 a[href]:eq(0)").text().trim());

				maMoto.setAnnee(docMoto.select("h1 a[href]:eq(1)").text().trim());

				maMoto.setModele(docMoto.select("h1").first().childNode(1).toString().trim());

				maMoto.setGenre(docMoto.select("td:contains(Category:) + td").text().trim());

				maMoto.setCylindree(docMoto.select("td:contains(Displacement:) + td").text().trim());

				maMoto.setTypeMoteur(docMoto.select("td:contains(Engine type:) + td").text().trim());

				maMoto.setPuissance(docMoto.select("td:contains(Power:) + td").text().trim());

				maMoto.setCouple(docMoto.select("td:contains(Torque:) + td").text().trim());

				maMoto.setTransmissionFinale(docMoto.select("td:contains(final drive:) + td").text().trim());

				maMoto.setNombreDeRapports(docMoto.select("td:contains(Gearbox:) + td").text().trim());

				maMoto.setPoids(docMoto.select("td:contains(Weight incl. oil, gas,) + td").text().trim());

				maMoto.setVolumeReservoir(docMoto.select("td:contains(Fuel capacity:) + td").text().trim());

				maMoto.setHauteurSelle(docMoto.select("td:contains(Seat height:) + td").text().trim());

				writer.write(maMoto.getMarque() + ";");
				writer.write(maMoto.getModele() + ";");
				writer.write(maMoto.getAnnee() + ";");
				writer.write(maMoto.getCylindree() + ";");
				writer.write(maMoto.getGenre() + ";");
				writer.write(maMoto.getTypeMoteur() + ";");
				writer.write(maMoto.getPuissance() + ";");
				writer.write(maMoto.getCouple() + ";");
				writer.write(maMoto.getTransmissionFinale() + ";");
				writer.write(maMoto.getNombreDeRapports() + ";");
				writer.write(maMoto.getPoids() + ";");
				writer.write(maMoto.getVolumeReservoir() + ";");
				writer.write(maMoto.getHauteurSelle());

				writer.newLine();

			}

		}

		writer.close();

		System.out.println("Fin");
	}

}
