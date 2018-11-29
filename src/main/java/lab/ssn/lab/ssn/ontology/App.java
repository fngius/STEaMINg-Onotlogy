package lab.ssn.lab.ssn.ontology;

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;


public class App 
{
    public static void main( String[] args ) throws OWLOntologyCreationException
    {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        
        String ontologyURI = "http://industry.ont";
        String ns = ontologyURI + "#";
        
        OWLOntology ontology = manager.createOntology(IRI.create(ontologyURI));

		
	    String pre_GEO = "http://www.opengis.net/ont/geosparql#";
	    String pre_TIME = "http://www.w3.org/2006/time#";
		String pre_SOSAOnt = "http://www.w3.org/ns/sosa/";
		String pre_SSNOnt = "http://www.w3.org/ns/ssn/";
		
		//OWLDatatype floatDatatype = factory.getFloatOWLDatatype();
		//OWLDatatype doubleDatatype = factory.getDoubleOWLDatatype();
		//OWLDatatype booleanDatatype = factory.getBooleanOWLDatatype();
		//OWLDatatype integerDatatype = factory.getIntegerOWLDatatype();
		OWLDatatype doubleDatatype = factory.getDoubleOWLDatatype();
		OWLDatatype dateTimeStamp = factory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTimeStamp"));
		OWLDatatype wkt = factory.getOWLDatatype(pre_GEO + "wktLiteral");
		
        OWLImportsDeclaration importDeclarationGEO = factory.getOWLImportsDeclaration(IRI.create(pre_GEO));
		OWLImportsDeclaration importDeclarationTIME = factory.getOWLImportsDeclaration(IRI.create(pre_TIME));
		OWLImportsDeclaration importDeclarationSSN = factory.getOWLImportsDeclaration(IRI.create(pre_SSNOnt));
		manager.applyChange(new AddImport(ontology, importDeclarationGEO));
		manager.applyChange(new AddImport(ontology, importDeclarationTIME));
		manager.applyChange(new AddImport(ontology, importDeclarationSSN));

		OWLClass Resource = factory.getOWLClass(IRI.create(ns,"Resource"));
		OWLClass Process = factory.getOWLClass(IRI.create(ns,"Process"));
		//ontology.add(factory.getOWLDeclarationAxiom(Resource));
		ontology.add(factory.getOWLDeclarationAxiom(Process));
		
		OWLClass Platform 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "Platform"));
		OWLClass System 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "System"));
		OWLClass Sensor 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "Sensor"));
		OWLClass Property 			= factory.getOWLClass(IRI.create(pre_SSNOnt  + "Property"));
		OWLClass ObservableProperty = factory.getOWLClass(IRI.create(pre_SOSAOnt + "ObservableProperty"));
		OWLClass Observation        = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Observation"));
		OWLClass FeatureOfInterest  = factory.getOWLClass(IRI.create(pre_SOSAOnt + "FeatureOfInterest"));
		OWLClass Result             = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Result"));
		
		ontology.add(factory.getOWLDeclarationAxiom(Result));
		ontology.add(factory.getOWLDeclarationAxiom(System));
		//ontology.add(factory.getOWLDeclarationAxiom(Platform));
		ontology.add(factory.getOWLSubClassOfAxiom(Sensor,System));
		ontology.add(factory.getOWLDeclarationAxiom(Property));
		ontology.add(factory.getOWLSubClassOfAxiom(ObservableProperty, Property));
		ontology.add(factory.getOWLDeclarationAxiom(Observation));
		ontology.add(factory.getOWLDeclarationAxiom(FeatureOfInterest));
		
		ontology.add(factory.getOWLEquivalentClassesAxiom(Resource, Platform));
		
		OWLClass Situation = factory.getOWLClass(IRI.create(ns,"Situation"));
		ontology.add(factory.getOWLSubClassOfAxiom(Situation, Observation));
		//ontology.add(factory.getOWLDeclarationAxiom(Situation));
		
		
		OWLObjectProperty hasResult = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "hasResult"));
		OWLObjectPropertyDomainAxiom hasResultdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasResult, Observation);
		OWLObjectPropertyRangeAxiom hasResultrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasResult, Result);
		ontology.add(factory.getOWLDeclarationAxiom(hasResult));
		ontology.add(hasResultdomainAxiom);
		ontology.add(hasResultrangeAxiom);
		
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

		//AddImport ALLEN Operators
		//OWLObjectProperty hasBeginning = factory.getOWLObjectProperty(IRI.create(pre_TIME + "hasBeginning"));
		//OWLObjectPropertyDomainAxiom hasBeginningdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasBeginning, TemporalEntity);
		//OWLObjectPropertyRangeAxiom hasBeginningrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasBeginning, Instant);
		//ontology.add(factory.getOWLDeclarationAxiom(hasBeginning));
		//ontology.add(hasBeginningdomainAxiom);
		//ontology.add(hasBeginningrangeAxiom);
		
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
		
		ontology.add(factory.getOWLSubClassOfAxiom(Resource, Feature));
		ontology.add(factory.getOWLSubClassOfAxiom(Platform, Feature));
		
		OWLDataProperty asWKT = factory.getOWLDataProperty(IRI.create(pre_GEO + "asWKT"));
		OWLDataPropertyDomainAxiom asWKTdomainAxiom = factory.getOWLDataPropertyDomainAxiom(asWKT, Geometry);
		OWLDataPropertyRangeAxiom asWKTrangeAxiom = factory.getOWLDataPropertyRangeAxiom(asWKT, wkt);
		ontology.add(factory.getOWLDeclarationAxiom(asWKT));
		ontology.add(asWKTdomainAxiom);
		ontology.add(asWKTrangeAxiom);
		
		OWLDataProperty hasSimpleResult = factory.getOWLDataProperty(IRI.create(pre_SOSAOnt + "hasSimpleResult"));
		OWLDataPropertyDomainAxiom hasSimpleResultdomainAxiom = factory.getOWLDataPropertyDomainAxiom(hasSimpleResult, Observation);
		OWLDataPropertyRangeAxiom hasSimpleResultrangeAxiom = factory.getOWLDataPropertyRangeAxiom(hasSimpleResult, doubleDatatype);
		ontology.add(factory.getOWLDeclarationAxiom(hasSimpleResult));
		ontology.add(hasSimpleResultdomainAxiom);
		ontology.add(hasSimpleResultrangeAxiom);
		
		OWLObjectProperty hasGeometry = factory.getOWLObjectProperty(IRI.create(pre_GEO + "hasGeometry"));
		OWLObjectPropertyDomainAxiom hasGeometrydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasGeometry, Feature);
		OWLObjectPropertyRangeAxiom hasGeometryrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasGeometry, Geometry);
		ontology.add(factory.getOWLDeclarationAxiom(hasGeometry));
		ontology.add(hasGeometrydomainAxiom);
		ontology.add(hasGeometryrangeAxiom);
		
		OWLObjectProperty rcc8ntpp = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8ntpp"));
		OWLObjectPropertyDomainAxiom rcc8ntppdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8ntpp, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8ntpprangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8ntpp, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8ntpp));
		ontology.add(rcc8ntppdomainAxiom);
		ontology.add(rcc8ntpprangeAxiom);
		
		OWLObjectProperty rcc8ec = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8ec"));
		OWLObjectPropertyDomainAxiom rcc8ecdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8ec, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8ecrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8ec, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8ec));
		ontology.add(rcc8ecdomainAxiom);
		ontology.add(rcc8ecrangeAxiom);
		
		OWLObjectProperty rcc8dc = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8dc"));
		OWLObjectPropertyDomainAxiom rcc8dcdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8dc, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8dcrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8dc, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8dc));
		ontology.add(rcc8dcdomainAxiom);
		ontology.add(rcc8dcrangeAxiom);
		
		OWLObjectProperty rcc8po = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8po"));
		OWLObjectPropertyDomainAxiom rcc8podomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8po, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8porangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8po, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8po));
		ontology.add(rcc8podomainAxiom);
		ontology.add(rcc8porangeAxiom);
		
		OWLObjectProperty rcc8tpp = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8tpp"));
		OWLObjectPropertyDomainAxiom rcc8tppdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8tpp, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8tpprangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8tpp, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8tpp));
		ontology.add(rcc8tppdomainAxiom);
		ontology.add(rcc8tpprangeAxiom);
		
		OWLObjectProperty rcc8tppi = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8tppi"));
		OWLObjectPropertyDomainAxiom rcc8tppidomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8tppi, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8tppirangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8tppi, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8tppi));
		ontology.add(rcc8tppidomainAxiom);
		ontology.add(rcc8tppirangeAxiom);

		OWLObjectProperty rcc8ntppi = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8ntppi"));
		OWLObjectPropertyDomainAxiom rcc8ntppidomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8ntppi, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8ntppirangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8ntppi, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8ntppi));
		ontology.add(rcc8ntppidomainAxiom);
		ontology.add(rcc8ntppirangeAxiom);
		
		OWLObjectProperty rcc8eq = factory.getOWLObjectProperty(IRI.create(pre_GEO + "rcc8eq"));
		OWLObjectPropertyDomainAxiom rcc8eqdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8eq, SpatialObject);
		OWLObjectPropertyRangeAxiom rcc8eqrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(rcc8eq, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(rcc8eq));
		ontology.add(rcc8eqdomainAxiom);
		ontology.add(rcc8eqrangeAxiom);
		
		OWLObjectProperty hasTime = factory.getOWLObjectProperty(IRI.create(ns,"hasTime"));
		OWLObjectPropertyDomainAxiom hasTimedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasTime, Observation);
		OWLObjectPropertyRangeAxiom hasTimerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasTime, TemporalEntity);
		ontology.add(factory.getOWLDeclarationAxiom(hasTime));
		ontology.add(hasTimedomainAxiom);
		ontology.add(hasTimerangeAxiom);

		OWLObjectProperty madeIn = factory.getOWLObjectProperty(IRI.create(ns,"madeIn"));
		OWLObjectPropertyDomainAxiom madeIndomainAxiom = factory.getOWLObjectPropertyDomainAxiom(madeIn, Observation);
		OWLObjectPropertyRangeAxiom madeInrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(madeIn, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(madeIn));
		ontology.add(madeIndomainAxiom);
		ontology.add(madeInrangeAxiom);

		OWLObjectProperty hasSensor = factory.getOWLObjectProperty(IRI.create(ns,"hasSensor"));
		OWLObjectPropertyDomainAxiom hasSensordomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasSensor, SpatialObject);
		OWLObjectPropertyRangeAxiom hasSensorrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasSensor, Sensor);
		ontology.add(factory.getOWLDeclarationAxiom(hasSensor));
		ontology.add(hasSensordomainAxiom);
		ontology.add(hasSensorrangeAxiom);
		
		OWLObjectProperty islocatedIn = factory.getOWLObjectProperty(IRI.create(ns,"islocatedIn"));
		OWLObjectPropertyDomainAxiom islocatedIndomainAxiom = factory.getOWLObjectPropertyDomainAxiom(islocatedIn, Resource);
		OWLObjectPropertyRangeAxiom islocatedInrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(islocatedIn, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(islocatedIn));
		ontology.add(islocatedIndomainAxiom);
		ontology.add(islocatedInrangeAxiom);

		OWLObjectProperty performs = factory.getOWLObjectProperty(IRI.create(ns,"performs"));
		OWLObjectPropertyDomainAxiom performsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(performs, Resource);
		OWLObjectPropertyRangeAxiom performsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(performs, Process);
		ontology.add(factory.getOWLDeclarationAxiom(performs));
		ontology.add(performsdomainAxiom);
		ontology.add(performsrangeAxiom);
		
		OWLObjectProperty hasDuration = factory.getOWLObjectProperty(IRI.create(ns,"hasDuration"));
		OWLObjectPropertyDomainAxiom hasDurationdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasDuration, Process);
		OWLObjectPropertyRangeAxiom hasDurationrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasDuration, TemporalEntity);
		ontology.add(factory.getOWLDeclarationAxiom(hasDuration));
		ontology.add(hasDurationdomainAxiom);
		ontology.add(hasDurationrangeAxiom);
		
		
		/* INDIVIDUALS */
		OWLIndividual Nacelle = factory.getOWLNamedIndividual(IRI.create(ns,"Nacelle"));
		OWLClassAssertionAxiom NucelleResource = factory.getOWLClassAssertionAxiom(Resource, Nacelle);
		ontology.add(NucelleResource);
		
		OWLIndividual S_temp_Nacelle = factory.getOWLNamedIndividual(IRI.create(ns,"S_temp_Nacelle"));
		OWLClassAssertionAxiom Sensor_temp_Nacelle = factory.getOWLClassAssertionAxiom(Sensor, S_temp_Nacelle);
		ontology.add(Sensor_temp_Nacelle);
		
		OWLIndividual Temp_Nacelle = factory.getOWLNamedIndividual(IRI.create(ns,"Temp_Nacelle"));
		OWLClassAssertionAxiom Temp_Nacelle_Prop = factory.getOWLClassAssertionAxiom(ObservableProperty, Temp_Nacelle);
		ontology.add(Temp_Nacelle_Prop);
		
		OWLObjectPropertyAssertionAxiom Nacelle_SensorTemp = factory.getOWLObjectPropertyAssertionAxiom(hosts, Nacelle, S_temp_Nacelle);
		ontology.add(Nacelle_SensorTemp);
		
		OWLIndividual Meteorology = factory.getOWLNamedIndividual(IRI.create(ns,"Meteorology"));
		OWLClassAssertionAxiom MeteorologyResource = factory.getOWLClassAssertionAxiom(Resource, Meteorology);
		ontology.add(MeteorologyResource);
		
		OWLIndividual S_temp_Ambient = factory.getOWLNamedIndividual(IRI.create(ns,"S_temp_Ambient"));
		OWLClassAssertionAxiom Sensor_temp_Ambient = factory.getOWLClassAssertionAxiom(Sensor, S_temp_Ambient);
		ontology.add(Sensor_temp_Ambient);
		
		OWLIndividual S_WindDir = factory.getOWLNamedIndividual(IRI.create(ns,"S_WindDir"));
		OWLClassAssertionAxiom Sensor_WindDir = factory.getOWLClassAssertionAxiom(Sensor, S_WindDir);
		ontology.add(Sensor_WindDir);
		
		OWLIndividual S_WindSpeed = factory.getOWLNamedIndividual(IRI.create(ns,"S_WindSpeed"));
		OWLClassAssertionAxiom Sensor_WindSpeed = factory.getOWLClassAssertionAxiom(Sensor, S_WindSpeed);
		ontology.add(Sensor_WindSpeed);
		
		OWLObjectPropertyAssertionAxiom Meteorolgy_SensorTemp = factory.getOWLObjectPropertyAssertionAxiom(hosts, Meteorology, S_temp_Ambient);
		ontology.add(Meteorolgy_SensorTemp);
		OWLObjectPropertyAssertionAxiom Meteorolgy_SensorWindDir = factory.getOWLObjectPropertyAssertionAxiom(hosts, Meteorology, S_WindDir);
		ontology.add(Meteorolgy_SensorWindDir);
		OWLObjectPropertyAssertionAxiom Meteorolgy_SensorWindSpeed = factory.getOWLObjectPropertyAssertionAxiom(hosts, Meteorology, S_WindSpeed);
		ontology.add(Meteorolgy_SensorWindSpeed);
		
        File fileformated = new File("/home/franco/Repositories/lab.ssn.ontology/test.owl");
        
        try {
			//ontology.saveOntology(System.out);
        	ontology.saveOntology(IRI.create(fileformated.toURI()));
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
		}

    

	/* Populate ontology with data from sensors */
	//OntologyAssistant oa = new OntologyAssistant();

	//FileInputStream fstream = null;
	//try {
	//	fstream = new FileInputStream("/home/franco/DataSets/SECOM/secom_final.data");
	//} catch (FileNotFoundException e) {
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
	//e.printStackTrace();
	//}
	//try {
	//	br.close();
	//} catch (IOException e) {
	//e.printStackTrace();
	//}
    }
}