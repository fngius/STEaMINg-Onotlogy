package lab.ssn.lab.ssn.ontology;

import java.io.File;
import java.util.Collection;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import ru.avicomp.ontapi.OntManagers;
import ru.avicomp.ontapi.OntologyManager;
import ru.avicomp.ontapi.OntologyModel;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String uri = "http://ontology.com/test";
        String ns = uri + "#";

        OntologyManager m = OntManagers.createONT();
        OWLDataFactory df = m.getOWLDataFactory();

        // compose ontology:
        OntologyModel o = m.createOntology(IRI.create(uri));
        OWLAnnotationProperty a1 = df.getOWLAnnotationProperty(IRI.create(ns, "prop1"));
        OWLAnnotationProperty a2 = df.getOWLAnnotationProperty(IRI.create(ns, "prop2"));
        OWLClass c1 = df.getOWLClass(IRI.create(ns, "class1"));
        OWLNamedIndividual i1 = df.getOWLNamedIndividual(IRI.create(ns, "indi1"));
        o.add(df.getOWLDeclarationAxiom(a1));
        o.add(df.getOWLDeclarationAxiom(a2));
        o.add(df.getOWLDeclarationAxiom(c1));
        o.add(df.getOWLAnnotationPropertyDomainAxiom(a1, c1.getIRI()));
        o.add(df.getOWLClassAssertionAxiom(c1, i1));
        o.add(df.getOWLSubAnnotationPropertyOfAxiom(a2, a1));
        o.add(df.getOWLAnnotationAssertionAxiom(a1, df.getOWLAnonymousIndividual(), i1.getIRI()));

        String pre_TIME = "http://www.w3.org/2006/time#";
		OWLImportsDeclaration importDeclarationTIME = m.getOWLDataFactory().getOWLImportsDeclaration(IRI.create(pre_TIME));
		m.applyChange(new AddImport(o, importDeclarationTIME));
		
		String pre_SOSAOnt = "http://www.w3.org/ns/sosa/";
		String pre_SSNOnt = "http://www.w3.org/ns/ssn/";
		OWLImportsDeclaration importDeclarationSSN = m.getOWLDataFactory().getOWLImportsDeclaration(IRI.create(pre_SSNOnt));
		m.applyChange(new AddImport(o, importDeclarationSSN));

		String sensor               = pre_SOSAOnt + "Sensor";
		OWLClass Sensor             = df.getOWLClass(IRI.create(sensor));
		OWLClass Property           = df.getOWLClass(IRI.create(pre_SSNOnt + "Property"));
		OWLClass ObservableProperty = df.getOWLClass(IRI.create(pre_SOSAOnt + "ObservableProperty"));
		OWLClass Observation        = df.getOWLClass(IRI.create(pre_SOSAOnt + "Observation"));
		OWLClass Result             = df.getOWLClass(IRI.create(pre_SOSAOnt + "Result"));
		
		o.add(df.getOWLDeclarationAxiom(Sensor));
		o.add(df.getOWLDeclarationAxiom(Property));
		o.add(df.getOWLDeclarationAxiom(ObservableProperty));
		o.add(df.getOWLDeclarationAxiom(Observation));
		o.add(df.getOWLDeclarationAxiom(Result));
		
		OWLObjectProperty observes = df.getOWLObjectProperty(IRI.create("http://www.w3.org/ns/sosa/observes"));
		o.add(df.getOWLDeclarationAxiom(observes));
		
		// Print ontology before updating:
        //o.axioms().forEach(System.out::println);
        try {
			o.saveOntology(System.out);
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
		}

        // SPARQL UPDATE: replace owl:AnnotationProperty -> owl:ObjectProperty
        UpdateAction.parseExecute(
                "DELETE { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#AnnotationProperty> } " +
                "INSERT { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#ObjectProperty> } " +
                "WHERE { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#AnnotationProperty> }",
                o.asGraphModel());

        // Print result:
        //o.axioms().forEach(System.out::println);
        try {
			o.saveOntology(System.out);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

        // SPARQL SELECT: just print all declarations:
        try (QueryExecution qexec = QueryExecutionFactory.create(QueryFactory.create("SELECT ?x ?y WHERE { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?y }"), o.asGraphModel())) {
            ResultSet res = qexec.execSelect();
            while (res.hasNext()) {
                //System.out.println(res.next());
            }
        }
    }
}