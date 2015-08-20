package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

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

		if (true)
		{

			// Téléchargement des fichiers absents
			for (int i = 1; i < 1718; i++)
			{

				boolean ATraiter = true;

				for (File fichier : dossier.listFiles())
				{
					if (fichier.getName().equals(i + ".txt"))
					{
						ATraiter = false;
						break;
					}
				}

				if (ATraiter)
				{

					URL adresse = new URL("http://www.motoprogress.com/fiche-moto.php?id_moto=" + i);

					BufferedReader in = new BufferedReader(new InputStreamReader(adresse.openStream()));

					BufferedWriter writer = new BufferedWriter(new FileWriter("save" + File.separator + i + ".txt"));

					String inputLine;
					while ((inputLine = in.readLine()) != null)
					{
						try
						{
							writer.write(inputLine);
							writer.newLine();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}

					in.close();
					writer.close();
				}
			}

		}

		Scanner sc;
		BufferedWriter writer = new BufferedWriter(new FileWriter(sortie));
		boolean FlagGenre = false;
		boolean FlagMarque = false;
		boolean FlagCylindree = false;
		boolean FlagModele = false;

		// Parcours des fichiers
		for (File fichier : dossier.listFiles())
		{
			sc = new Scanner(new BufferedReader(new FileReader(fichier)));
			while (sc.hasNext())
			{
				String line = sc.nextLine();

				// Ecriture
				if (FlagGenre)
				{
					writer.write(line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")).trim() + ",");
					FlagGenre = false;
				}
				if (FlagMarque)
				{
					writer.write(line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")).replace("<br />", "")
							+ ",");
					FlagMarque = false;
				}
				if (FlagCylindree)
				{
					writer.write(
							line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")).replace("  cm&sup3", ""));
					FlagCylindree = false;
				}
				if (FlagModele)
				{
					writer.write(line.substring(line.indexOf("<td>") + 4, line.indexOf("</td>")).replace("<br />", "")
							+ ",");
					FlagModele = false;
				}

				// Flag
				if (line.contains("<td>Type : </td>"))
				{
					FlagGenre = true;
				}
				if (line.contains("<td>Marque : </td>"))
				{
					FlagMarque = true;
				}
				if (line.contains("<td>Cylindr&eacute;e : </td>"))
				{
					FlagCylindree = true;
				}
				if (line.contains("<td>Modele : </td>"))
				{
					FlagModele = true;
				}

			}
			writer.newLine();
		}

		writer.close();

		System.out.println("Fin.");
	}

}
