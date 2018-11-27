package lab.ssn.lab.ssn.ontology;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.vocabulary.RDFS;
import org.apache.xerces.impl.dtd.models.DFAContentModel;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import ru.avicomp.ontapi.OntManagers;
import ru.avicomp.ontapi.OntologyManager;
import ru.avicomp.ontapi.OntologyModel;


public class App 
{
    public static void main( String[] args ) throws OWLOntologyCreationException
    {
        String uri = "http://ontology.com/test";
        //String ns = uri + "#";

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        
        String ontologyURI = "http://industry.ont";
        String ns = ontologyURI + "#";
        
        OWLOntology ontology = manager.createOntology(IRI.create(ontologyURI));

		OWLDatatype integerDatatype = factory.getIntegerOWLDatatype();
		OWLDatatype floatDatatype = factory.getFloatOWLDatatype();
		OWLDatatype doubleDatatype = factory.getDoubleOWLDatatype();
		OWLDatatype booleanDatatype = factory.getBooleanOWLDatatype();
	    //OWLDatatype dateTime = factory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTime"));
	    OWLDatatype dateTimeStamp = factory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTimeStamp"));
		
	    String pre_GEO = "http://www.opengis.net/ont/geosparql#";
	    String pre_TIME = "http://www.w3.org/2006/time#";
		String pre_SOSAOnt = "http://www.w3.org/ns/sosa/";
		String pre_SSNOnt = "http://www.w3.org/ns/ssn/";

        OWLImportsDeclaration importDeclarationGEO = factory.getOWLImportsDeclaration(IRI.create(pre_GEO));
		OWLImportsDeclaration importDeclarationTIME = factory.getOWLImportsDeclaration(IRI.create(pre_TIME));
		OWLImportsDeclaration importDeclarationSSN = factory.getOWLImportsDeclaration(IRI.create(pre_SSNOnt));
		manager.applyChange(new AddImport(ontology, importDeclarationGEO));
		manager.applyChange(new AddImport(ontology, importDeclarationTIME));
		manager.applyChange(new AddImport(ontology, importDeclarationSSN));

		OWLClass Resource = factory.getOWLClass(IRI.create(ns,"Resource"));
		
		OWLClass Process = factory.getOWLClass(IRI.create(ns,"Process"));
		
		ontology.add(factory.getOWLDeclarationAxiom(Resource));
		ontology.add(factory.getOWLDeclarationAxiom(Process));
		
		OWLClass Platform 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "Platform"));
		OWLClass System 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "System"));
		OWLClass Sensor 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "Sensor"));
		OWLClass Property 			= factory.getOWLClass(IRI.create(pre_SSNOnt  + "Property"));
		OWLClass ObservableProperty = factory.getOWLClass(IRI.create(pre_SOSAOnt + "ObservableProperty"));
		OWLClass Observation        = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Observation"));
		OWLClass FeatureOfInterest  = factory.getOWLClass(IRI.create(pre_SOSAOnt + "FeatureOfInterest"));
		
		ontology.add(factory.getOWLDeclarationAxiom(System));
		ontology.add(factory.getOWLDeclarationAxiom(Platform));
		ontology.add(factory.getOWLSubClassOfAxiom(Sensor,System));
		ontology.add(factory.getOWLDeclarationAxiom(Property));
		ontology.add(factory.getOWLSubClassOfAxiom(ObservableProperty, Property));
		ontology.add(factory.getOWLDeclarationAxiom(Observation));
		ontology.add(factory.getOWLDeclarationAxiom(FeatureOfInterest));
		
		OWLObjectProperty madeObservation = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "madeObservation"));
		OWLObjectPropertyDomainAxiom domainAxiom = factory.getOWLObjectPropertyDomainAxiom(madeObservation, Sensor);
		OWLObjectPropertyRangeAxiom rangeAxiom = factory.getOWLObjectPropertyRangeAxiom(madeObservation, Observation);
		ontology.add(factory.getOWLDeclarationAxiom(madeObservation));
		ontology.add(domainAxiom);
		ontology.add(rangeAxiom);
		
		OWLObjectProperty observedProperty = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "observedProperty"));
		OWLObjectPropertyDomainAxiom observedPropertydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(observedProperty, Observation);
		OWLObjectPropertyRangeAxiom observedPropertyrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(observedProperty, ObservableProperty);
		ontology.add(factory.getOWLDeclarationAxiom(observedProperty));
		ontology.add(observedPropertydomainAxiom);
		ontology.add(observedPropertyrangeAxiom);
		
		OWLObjectProperty hasFeatureOfInterest = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "hasFeatureOfInterest"));
		OWLObjectPropertyDomainAxiom hasFeatureOfInterestdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasFeatureOfInterest, Observation);
		OWLObjectPropertyRangeAxiom hasFeatureOfInterestrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasFeatureOfInterest, FeatureOfInterest);
		ontology.add(factory.getOWLDeclarationAxiom(observedProperty));
		ontology.add(hasFeatureOfInterestdomainAxiom);
		ontology.add(hasFeatureOfInterestrangeAxiom);
		
		OWLObjectProperty hosts = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "hosts"));
		OWLObjectPropertyDomainAxiom hostsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hosts, Platform);
		OWLObjectPropertyRangeAxiom hostsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hosts, Sensor);
		ontology.add(factory.getOWLDeclarationAxiom(hosts));
		ontology.add(hostsdomainAxiom);
		ontology.add(hostsrangeAxiom);

		OWLObjectProperty hasProperty = factory.getOWLObjectProperty(IRI.create(pre_SSNOnt + "hasProperty"));
		OWLObjectPropertyDomainAxiom hasPropertydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasProperty, FeatureOfInterest);
		OWLObjectPropertyRangeAxiom hasPropertyrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasProperty, Property);
		ontology.add(factory.getOWLDeclarationAxiom(hasProperty));
		ontology.add(hasPropertydomainAxiom);
		ontology.add(hasPropertyrangeAxiom);
		
		OWLClass TemporalEntity = factory.getOWLClass(IRI.create(pre_TIME + "TemporalEntity"));
		OWLClass Instant        = factory.getOWLClass(IRI.create(pre_TIME + "Instant"));
		OWLClass Interval       = factory.getOWLClass(IRI.create(pre_TIME + "Interval"));
		
		ontology.add(factory.getOWLDeclarationAxiom(TemporalEntity));
		ontology.add(factory.getOWLSubClassOfAxiom(Instant, TemporalEntity));
		ontology.add(factory.getOWLSubClassOfAxiom(Interval, TemporalEntity));

		OWLObjectProperty hasBeginning = factory.getOWLObjectProperty(IRI.create(pre_TIME + "hasBeginning"));
		OWLObjectPropertyDomainAxiom hasBeginningdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasBeginning, TemporalEntity);
		OWLObjectPropertyRangeAxiom hasBeginningrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasBeginning, Instant);
		ontology.add(factory.getOWLDeclarationAxiom(hasBeginning));
		ontology.add(hasBeginningdomainAxiom);
		ontology.add(hasBeginningrangeAxiom);
		
		OWLObjectProperty hasEnd = factory.getOWLObjectProperty(IRI.create(pre_TIME + "hasEnd"));
		OWLObjectPropertyDomainAxiom hasEnddomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasEnd, TemporalEntity);
		OWLObjectPropertyRangeAxiom hasEndrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasEnd, Instant);
		ontology.add(factory.getOWLDeclarationAxiom(hasEnd));
		ontology.add(hasEnddomainAxiom);
		ontology.add(hasEndrangeAxiom);
		
		OWLDataProperty inXSDDateTimeStamp = factory.getOWLDataProperty(IRI.create(pre_TIME + "inXSDDateTimeStamp"));
		OWLDataPropertyDomainAxiom inXSDDateTimeStampdomainAxiom = factory.getOWLDataPropertyDomainAxiom(inXSDDateTimeStamp, Instant);
		OWLDataPropertyRangeAxiom inXSDDateTimeStamprangeAxiom = factory.getOWLDataPropertyRangeAxiom(inXSDDateTimeStamp, dateTimeStamp);
		ontology.add(factory.getOWLDeclarationAxiom(inXSDDateTimeStamp));
		ontology.add(inXSDDateTimeStampdomainAxiom);
		ontology.add(inXSDDateTimeStamprangeAxiom);
		
		OWLClass SpatialObject = factory.getOWLClass(IRI.create(pre_GEO + "SpatialObject"));
		OWLClass Feature = factory.getOWLClass(IRI.create(pre_GEO + "Feature"));
		OWLClass Geometry = factory.getOWLClass(IRI.create(pre_GEO + "Geometry"));
		
		ontology.add(factory.getOWLDeclarationAxiom(SpatialObject));
		ontology.add(factory.getOWLSubClassOfAxiom(Feature, SpatialObject));
		ontology.add(factory.getOWLSubClassOfAxiom(Geometry, SpatialObject));
		
		OWLObjectProperty hasGeometry = factory.getOWLObjectProperty(IRI.create(pre_GEO + "hasGeometry"));
		OWLObjectPropertyDomainAxiom hasGeometrydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasGeometry, Feature);
		OWLObjectPropertyRangeAxiom hasGeometryrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasGeometry, Geometry);
		ontology.add(factory.getOWLDeclarationAxiom(hasGeometry));
		ontology.add(hasGeometrydomainAxiom);
		ontology.add(hasGeometryrangeAxiom);
		

		
		OWLDataProperty resultTime = factory.getOWLDataProperty(IRI.create(pre_SOSAOnt + "resultTime"));
		ontology.add(factory.getOWLDeclarationAxiom(resultTime));
		
        //OntologyManager m = OntManagers.createONT();
        //OWLDataFactory df = m.getOWLDataFactory();

        // compose ontology:
        //OntologyModel o = m.createOntology(IRI.create(uri));
        //OWLAnnotationProperty a1 = df.getOWLAnnotationProperty(IRI.create(ns, "prop1"));
        //OWLAnnotationProperty a2 = df.getOWLAnnotationProperty(IRI.create(ns, "prop2"));
        //OWLClass c1 = df.getOWLClass(IRI.create(ns, "class1"));
        //OWLNamedIndividual i1 = df.getOWLNamedIndividual(IRI.create(ns, "indi1"));
        //o.add(df.getOWLDeclarationAxiom(a1));
        //o.add(df.getOWLDeclarationAxiom(a2));
        //o.add(df.getOWLDeclarationAxiom(c1));
        //o.add(df.getOWLAnnotationPropertyDomainAxiom(a1, c1.getIRI()));
        //o.add(df.getOWLClassAssertionAxiom(c1, i1));
        //o.add(df.getOWLSubAnnotationPropertyOfAxiom(a2, a1));
        //o.add(df.getOWLAnnotationAssertionAxiom(a1, df.getOWLAnonymousIndividual(), i1.getIRI()));

        //String pre_TIME = "http://www.w3.org/2006/time#";
		//OWLImportsDeclaration importDeclarationTIME = m.getOWLDataFactory().getOWLImportsDeclaration(IRI.create(pre_TIME));
		//m.applyChange(new AddImport(o, importDeclarationTIME));
		
        
        //Create a file for the new format
        File fileformated = new File("/home/franco/Repositories/lab.ssn.ontology/test.owl");
        
        try {
			//ontology.saveOntology(System.out);
        	ontology.saveOntology(IRI.create(fileformated.toURI()));
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
		}

    

	/* Populate ontology with data from sensors */
	OntologyAssistant oa = new OntologyAssistant();

	//FileInputStream fstream = null;
	//try {
	//	fstream = new FileInputStream("/home/franco/DataSets/SECOM/secom_final.data");
	//} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
	//	e.printStackTrace();
	//}
	//BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	//String strLine;
	//int i = 0;
	//int cont = 0;
	//try {
	//while (((strLine = br.readLine()) != null) && (cont<10))   {
	//	cont++;
	//	i++;
	//	OWLNamedIndividual O1 = factory.getOWLNamedIndividual(IRI.create(ns, "OBS1"));
	//	String dString = strLine.split(" ")[1];
	//	String newD = dString.split("-")[2] + "-" + dString.split("-")[1] + "-" + dString.split("-")[0];
			
			//OWLLiteral date = df.getOWLLiteral(newD + "T" + strLine.split(" ")[2]+".000",OWL2Datatype.XSD_DATE_TIME);
		    //oa.createIndividual(o, m, df, contextOntIRI + "#TI" + Integer.toString(i), ValidInstant);
		    //oa.assignValueToDataTypeProperty(ontology, manager, factory, hasTimeT, factory.getOWLNamedIndividual(IRI.create(contextOntIRI + "#TI" + Integer.toString(i))), date);
		    //oa.createIndividual(ontology, manager, factory, contextOntIRI + "#O" + Integer.toString(i) , Observation);
			//oa.relateIndividuals(ontology, manager, factory, madeBySensor, factory.getOWLNamedIndividual(IRI.create(contextOntIRI + "#O" + Integer.toString(i))), Sensor_1);
			//oa.relateIndividuals(ontology, manager, factory, observedProperty, factory.getOWLNamedIndividual(IRI.create(contextOntIRI + "#O" + Integer.toString(i))), OProperty_1);
			//oa.assignValueToDataTypeProperty(ontology, manager, factory, hasSimpleResult, factory.getOWLNamedIndividual(IRI.create(contextOntIRI + "#O" + Integer.toString(i))), factory.getOWLLiteral(Double.parseDouble(strLine.split(" ")[3])));
			//oa.assignValueToDataTypeProperty(ontology, manager, factory, resultTime, factory.getOWLNamedIndividual(IRI.create(contextOntIRI + "#O" + Integer.toString(i))), date);
			//oa.relateIndividuals(ontology, manager, factory, hasTimeObs, factory.getOWLNamedIndividual(IRI.create(contextOntIRI + "#O" + Integer.toString(i))), factory.getOWLNamedIndividual(IRI.create(contextOntIRI + "#TI" + Integer.toString(i)))); 

	//		System.out.println (newD+ "T" + strLine.split(" ")[2]);
				
				//SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);   			
				//ruleEngine.infer();

				//ruleEngine.importAssertedOWLAxioms();
				//ruleEngine.run();
				//ruleEngine.exportInferredOWLAxioms();
	//}
	//} catch (IOException e) {
		// TODO Auto-generated catch block
	//e.printStackTrace();
	//}
	//try {
	//	br.close();
	//} catch (IOException e) {
		// TODO Auto-generated catch block
	//e.printStackTrace();
	//}
    }
}