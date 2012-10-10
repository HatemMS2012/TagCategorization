package hms.categorizer.dbpedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class DBPediaTagCategorizer {

	//some comments here
	public static void searcDBPedia(String word, String type)
			throws MalformedURLException, IOException {

		URLConnection uc = null; 

		uc = new URL("http://localhost:8080/openrdfWB/repositories/dbo/query?queryLn=SPARQL&query=PREFIX%20%3A%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2F%3E%0APREFIX%20rdfs%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0APREFIX%20xsd%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0APREFIX%20owl%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0A%0A%0ASELECT%20%20*%0AWHERE%20%7B%0A%0A%20%20%20%20%3Fcountry%20a%20%20%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FCountry%3E%0A%7D%0ALIMIT%205&limit=10&infer=true")
				.openConnection();

		System.out.println(uc);

		BufferedReader br = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));

		String str;

		StringBuffer sb = new StringBuffer();

		while ((str = br.readLine()) != null) {

			sb.append(str);
			sb.append("\n");

		}

		br.close();
		System.out.println("[Result]: " + sb);

	}

	public static void main(String[] args) throws MalformedURLException,
			IOException, RepositoryException, MalformedQueryException, QueryEvaluationException {

		queryByClass("City", "Berlin");
		System.out.println("-------------------------");
		queryByClass("Place", "Berlin");

	}

	public static void queryByClass(String type, String query) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException {

		String sesameServer = "http://localhost:8080/openrdfSesame";
		String repositoryID = "dbo";

		Repository myRepository = new HTTPRepository(sesameServer, repositoryID);
		myRepository.initialize();

		RepositoryConnection con = myRepository.getConnection();
		String queryString = "SELECT  * WHERE { ?country a  <http://dbpedia.org/ontology/"+type+">.  " +
				                              " FILTER regex(str(?country), '(^|\\\\W)"+query+"(\\\\W|$)',\"i\").} " + 
				                              "LIMIT 10";
		
		System.out.println("\\\\W");
		System.out.println("......................");
		System.out.println(queryString);

		TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL,
				queryString);
		TupleQueryResult result = tupleQuery.evaluate();

		while(result.hasNext()){
			BindingSet r = result.next();
			System.out.println(r.getBinding("country"));
		}
		result.close();

		con.close();
	}
}
