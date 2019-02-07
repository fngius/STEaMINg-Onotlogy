package ontoInd4;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.crypto.Mac;

import org.apache.commons.io.output.ThresholdingOutputStream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;


public class App 
{
    @SuppressWarnings("null")
	public static void main( String[] args ) throws OWLOntologyCreationException
    {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        
        String ontologyURI = "http://industry.ont";
        String ns = ontologyURI + "#";
        
        String ontologyURIProcess = "http://industryProcess.ont";
        String nsProcess = ontologyURIProcess + "#";
        
        String ontologyURISituation = "http://industrySituation.ont";
        String nsSituation = ontologyURISituation + "#";
        
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
		OWLDatatype gml = factory.getOWLDatatype(pre_GEO + "gmlLiteral");

        OWLImportsDeclaration importDeclarationGEO = factory.getOWLImportsDeclaration(IRI.create(pre_GEO));
		OWLImportsDeclaration importDeclarationTIME = factory.getOWLImportsDeclaration(IRI.create(pre_TIME));
		OWLImportsDeclaration importDeclarationSSN = factory.getOWLImportsDeclaration(IRI.create(pre_SSNOnt));
		manager.applyChange(new AddImport(ontology, importDeclarationGEO));
		manager.applyChange(new AddImport(ontology, importDeclarationTIME));
		manager.applyChange(new AddImport(ontology, importDeclarationSSN));

		OWLClass Process = factory.getOWLClass(IRI.create(nsProcess,"Process"));
		OWLClass ManufacturingProcess = factory.getOWLClass(IRI.create(nsProcess,"ManufacturingProcess"));
		OWLClass LogisticProcess = factory.getOWLClass(IRI.create(nsProcess,"LogisticProcess"));
		OWLClass HumanProcess = factory.getOWLClass(IRI.create(nsProcess,"HumanProcess"));

		ontology.add(factory.getOWLSubClassOfAxiom(ManufacturingProcess,Process));
		ontology.add(factory.getOWLSubClassOfAxiom(LogisticProcess,Process));
		ontology.add(factory.getOWLSubClassOfAxiom(HumanProcess,Process));
		
		OWLClass Resource = factory.getOWLClass(IRI.create(ns,"Resource"));
		OWLClass Product = factory.getOWLClass(IRI.create(ns,"Product"));
		OWLClass Part = factory.getOWLClass(IRI.create(ns,"Part"));
		OWLClass Staff = factory.getOWLClass(IRI.create(ns,"Staff"));
		OWLClass Operator = factory.getOWLClass(IRI.create(ns,"Operator"));
		OWLClass Technician = factory.getOWLClass(IRI.create(ns,"Technician"));
		OWLClass Manager = factory.getOWLClass(IRI.create(ns,"Manager"));
		OWLClass ManufacturingFacility = factory.getOWLClass(IRI.create(ns,"ManufacturingFacility"));
		OWLClass Line = factory.getOWLClass(IRI.create(ns,"Line"));
		OWLClass Cell = factory.getOWLClass(IRI.create(ns,"Cell"));
		OWLClass WorkStation = factory.getOWLClass(IRI.create(ns,"WorkStation"));
		OWLClass Machine = factory.getOWLClass(IRI.create(ns,"Machine"));
		//ontology.add(factory.getOWLDeclarationAxiom(Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Product,Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Part,Product));
		ontology.add(factory.getOWLSubClassOfAxiom(Staff,Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Operator,Staff));
		ontology.add(factory.getOWLSubClassOfAxiom(Technician,Staff));
		ontology.add(factory.getOWLSubClassOfAxiom(Manager,Staff));
		ontology.add(factory.getOWLSubClassOfAxiom(ManufacturingFacility,Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Line,ManufacturingFacility));
		ontology.add(factory.getOWLSubClassOfAxiom(Cell,ManufacturingFacility));
		ontology.add(factory.getOWLSubClassOfAxiom(WorkStation,ManufacturingFacility));
		ontology.add(factory.getOWLSubClassOfAxiom(Machine,ManufacturingFacility));
		
        List<OWLClass> list = Arrays.asList(Machine,WorkStation,Cell,Line);
		ontology.add(factory.getOWLDisjointClassesAxiom(list));

		ontology.add(factory.getOWLDeclarationAxiom(Process));
		
		OWLClass Platform 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "Platform"));
		//OWLClass System 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "System"));
		OWLClass Sensor 			= factory.getOWLClass(IRI.create(pre_SOSAOnt + "Sensor"));
		OWLClass Property 			= factory.getOWLClass(IRI.create(pre_SSNOnt  + "Property"));
		OWLClass ObservableProperty = factory.getOWLClass(IRI.create(pre_SOSAOnt + "ObservableProperty"));
		OWLClass Observation        = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Observation"));
		OWLClass FeatureOfInterest  = factory.getOWLClass(IRI.create(pre_SOSAOnt + "FeatureOfInterest"));
		OWLClass Result             = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Result"));
		
		ontology.add(factory.getOWLDeclarationAxiom(Result));
		//ontology.add(factory.getOWLDeclarationAxiom(System));
		//ontology.add(factory.getOWLDeclarationAxiom(Platform));
		//ontology.add(factory.getOWLSubClassOfAxiom(Sensor,System));
		ontology.add(factory.getOWLDeclarationAxiom(Sensor));
		ontology.add(factory.getOWLDeclarationAxiom(Property));
		ontology.add(factory.getOWLSubClassOfAxiom(ObservableProperty, Property));
		ontology.add(factory.getOWLDeclarationAxiom(Observation));
		ontology.add(factory.getOWLDeclarationAxiom(FeatureOfInterest));
		
		ontology.add(factory.getOWLEquivalentClassesAxiom(Resource, Platform));
		
		OWLClass Situation = factory.getOWLClass(IRI.create(nsSituation,"Situation"));
		ontology.add(factory.getOWLSubClassOfAxiom(Situation, Observation));
		//ontology.add(factory.getOWLDeclarationAxiom(Situation));		


		OWLObjectProperty isPartOf = factory.getOWLObjectProperty(IRI.create(ns,"isPartOf"));
		OWLObjectPropertyDomainAxiom isPartOfdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(isPartOf, ManufacturingFacility);
		OWLObjectPropertyRangeAxiom isPartOfrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(isPartOf, ManufacturingFacility);
		ontology.add(factory.getOWLDeclarationAxiom(isPartOf));
		ontology.add(isPartOfdomainAxiom);
		ontology.add(isPartOfrangeAxiom);

		/*
		OWLObjectProperty MisPartOfW = factory.getOWLObjectProperty(IRI.create(ns,"MisPartOfW"));
		OWLObjectPropertyDomainAxiom MisPartOfWdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(MisPartOfW, Machine);
		OWLObjectPropertyRangeAxiom MisPartOfWrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(MisPartOfW, WorkStation);
		ontology.add(factory.getOWLDeclarationAxiom(MisPartOfW));
		ontology.add(MisPartOfWdomainAxiom);
		ontology.add(MisPartOfWrangeAxiom);
		
		OWLObjectProperty WisPartOfC = factory.getOWLObjectProperty(IRI.create(ns,"WisPartOfC"));
		OWLObjectPropertyDomainAxiom WisPartOfCdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(WisPartOfC, WorkStation);
		OWLObjectPropertyRangeAxiom WisPartOfCrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(WisPartOfC, Cell);
		ontology.add(factory.getOWLDeclarationAxiom(WisPartOfC));
		ontology.add(WisPartOfCdomainAxiom);
		ontology.add(WisPartOfCrangeAxiom);
		
		OWLObjectProperty CisPartOfL = factory.getOWLObjectProperty(IRI.create(ns,"CisPartOfL"));
		OWLObjectPropertyDomainAxiom CisPartOfLdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(CisPartOfL, Cell);
		OWLObjectPropertyRangeAxiom CisPartOfLrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(CisPartOfL, Line);
		ontology.add(factory.getOWLDeclarationAxiom(CisPartOfL));
		ontology.add(CisPartOfLdomainAxiom);
		ontology.add(CisPartOfLrangeAxiom);
		*/
		OWLObjectProperty operates = factory.getOWLObjectProperty(IRI.create(ns,"operates"));
		OWLObjectPropertyDomainAxiom operatesdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(operates, Staff);
		OWLObjectPropertyRangeAxiom operatesrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(operates, ManufacturingFacility);
		ontology.add(factory.getOWLDeclarationAxiom(operates));
		ontology.add(operatesdomainAxiom);
		ontology.add(operatesrangeAxiom);
		
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
		
		OWLObjectProperty after = factory.getOWLObjectProperty(IRI.create(pre_TIME + "after"));
		OWLObjectPropertyDomainAxiom afterdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(after, TemporalEntity);
		OWLObjectPropertyRangeAxiom afterrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(after, TemporalEntity);
		ontology.add(factory.getOWLDeclarationAxiom(after));
		ontology.add(afterdomainAxiom);
		ontology.add(afterrangeAxiom);
		
		OWLObjectProperty before = factory.getOWLObjectProperty(IRI.create(pre_TIME + "before"));
		OWLObjectPropertyDomainAxiom beforedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(before, TemporalEntity);
		OWLObjectPropertyRangeAxiom beforerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(before, TemporalEntity);
		ontology.add(factory.getOWLDeclarationAxiom(before));
		ontology.add(beforedomainAxiom);
		ontology.add(beforerangeAxiom);
		
		OWLObjectProperty inside = factory.getOWLObjectProperty(IRI.create(pre_TIME + "inside"));
		OWLObjectPropertyDomainAxiom insidedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(inside, Interval);
		OWLObjectPropertyRangeAxiom insiderangeAxiom = factory.getOWLObjectPropertyRangeAxiom(inside, Instant);
		ontology.add(factory.getOWLDeclarationAxiom(inside));
		ontology.add(insidedomainAxiom);
		ontology.add(insiderangeAxiom);
		
		OWLObjectProperty intervalIn = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalIn"));
		OWLObjectPropertyDomainAxiom intervalIndomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalIn, Interval);
		OWLObjectPropertyRangeAxiom intervalInrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalIn, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalIn));
		ontology.add(intervalIndomainAxiom);
		ontology.add(intervalInrangeAxiom);
		
		OWLObjectProperty intervalDisjoint = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalDisjoint"));
		OWLObjectPropertyDomainAxiom intervalDisjointdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalDisjoint, Interval);
		OWLObjectPropertyRangeAxiom intervalDisjointrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalDisjoint, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalDisjoint));
		ontology.add(intervalDisjointdomainAxiom);
		ontology.add(intervalDisjointrangeAxiom);
		
		OWLObjectProperty intervalEquals = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalEquals"));
		OWLObjectPropertyDomainAxiom intervalEqualsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalEquals, Interval);
		OWLObjectPropertyRangeAxiom intervalEqualsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalEquals, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalEquals));
		ontology.add(intervalEqualsdomainAxiom);
		ontology.add(intervalEqualsrangeAxiom);
		
		OWLObjectProperty intervalFinishedBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalFinishedBy"));
		OWLObjectPropertyDomainAxiom intervalFinishedBydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalFinishedBy, Interval);
		OWLObjectPropertyRangeAxiom intervalFinishedByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalFinishedBy, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalFinishedBy));
		ontology.add(intervalFinishedBydomainAxiom);
		ontology.add(intervalFinishedByrangeAxiom);
		
		OWLObjectProperty intervalFinishes = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalFinishes"));
		OWLObjectPropertyDomainAxiom intervalFinishesdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalFinishes, Interval);
		OWLObjectPropertyRangeAxiom intervalFinishesrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalFinishes, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalFinishes));
		ontology.add(intervalFinishesdomainAxiom);
		ontology.add(intervalFinishesrangeAxiom);
		
		OWLObjectProperty intervalContains = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalContains"));
		OWLObjectPropertyDomainAxiom intervalContainsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalContains, Interval);
		OWLObjectPropertyRangeAxiom intervalContainsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalContains, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalContains));
		ontology.add(intervalContainsdomainAxiom);
		ontology.add(intervalContainsrangeAxiom);
		
		OWLObjectProperty intervalDuring = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalDuring"));
		OWLObjectPropertyDomainAxiom intervalDuringdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalDuring, Interval);
		OWLObjectPropertyRangeAxiom intervalDuringrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalDuring, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalDuring));
		ontology.add(intervalDuringdomainAxiom);
		ontology.add(intervalDuringrangeAxiom);
		
		OWLObjectProperty intervalStartedBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalStartedBy"));
		OWLObjectPropertyDomainAxiom intervalStartedBydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalStartedBy, Interval);
		OWLObjectPropertyRangeAxiom intervalStartedByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalStartedBy, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalStartedBy));
		ontology.add(intervalStartedBydomainAxiom);
		ontology.add(intervalStartedByrangeAxiom);
		
		OWLObjectProperty intervalStarts = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalStarts"));
		OWLObjectPropertyDomainAxiom intervalStartsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalStarts, Interval);
		OWLObjectPropertyRangeAxiom intervalStartsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalStarts, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalStarts));
		ontology.add(intervalStartsdomainAxiom);
		ontology.add(intervalStartsrangeAxiom);
		
		OWLObjectProperty intervalOverlappedBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalOverlaps"));
		OWLObjectPropertyDomainAxiom intervalOverlappedBydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalOverlappedBy, Interval);
		OWLObjectPropertyRangeAxiom intervalOverlappedByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalOverlappedBy, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalOverlappedBy));
		ontology.add(intervalOverlappedBydomainAxiom);
		ontology.add(intervalOverlappedByrangeAxiom);
		
		OWLObjectProperty intervalOverlaps = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalOverlaps"));
		OWLObjectPropertyDomainAxiom intervalOverlapsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalOverlaps, Interval);
		OWLObjectPropertyRangeAxiom intervalOverlapsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalOverlaps, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalOverlaps));
		ontology.add(intervalOverlapsdomainAxiom);
		ontology.add(intervalOverlapsrangeAxiom);
		
		OWLObjectProperty intervalMetBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalMetBy"));
		OWLObjectPropertyDomainAxiom intervalMetBydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalMetBy, Interval);
		OWLObjectPropertyRangeAxiom intervalMetByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalMetBy, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalMetBy));
		ontology.add(intervalMetBydomainAxiom);
		ontology.add(intervalMetByrangeAxiom);
		
		OWLObjectProperty intervalMeets = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalMeets"));
		OWLObjectPropertyDomainAxiom intervalMeetsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalMeets, Interval);
		OWLObjectPropertyRangeAxiom intervalMeetsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalMeets, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalMeets));
		ontology.add(intervalMeetsdomainAxiom);
		ontology.add(intervalMeetsrangeAxiom);
		
		OWLObjectProperty intervalAfter = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalAfter"));
		OWLObjectPropertyDomainAxiom intervalAfterdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalAfter, Interval);
		OWLObjectPropertyRangeAxiom intervalAfterrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalAfter, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalAfter));
		ontology.add(intervalAfterdomainAxiom);
		ontology.add(intervalAfterrangeAxiom);
		
		OWLObjectProperty intervalBefore = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalBefore"));
		OWLObjectPropertyDomainAxiom intervalBeforedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalBefore, Interval);
		OWLObjectPropertyRangeAxiom intervalBeforerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalBefore, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalBefore));
		ontology.add(intervalBeforedomainAxiom);
		ontology.add(intervalBeforerangeAxiom);
		
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

		OWLDataProperty asGML = factory.getOWLDataProperty(IRI.create(pre_GEO + "asGML"));
		OWLDataPropertyDomainAxiom asGMLdomainAxiom = factory.getOWLDataPropertyDomainAxiom(asGML, Geometry);
		OWLDataPropertyRangeAxiom asGMLrangeAxiom = factory.getOWLDataPropertyRangeAxiom(asGML, gml);
		ontology.add(factory.getOWLDeclarationAxiom(asGML));
		ontology.add(asGMLdomainAxiom);
		ontology.add(asGMLrangeAxiom);
		
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
		OWLObjectPropertyAssertionAxiom obsProp_NacelleTemp_SensorTemp = factory.getOWLObjectPropertyAssertionAxiom(observedProperty, S_temp_Nacelle, Temp_Nacelle);
		ontology.add(obsProp_NacelleTemp_SensorTemp);

		OWLIndividual Gearbox = factory.getOWLNamedIndividual(IRI.create(ns,"Gearbox"));
		OWLClassAssertionAxiom GearboxResource = factory.getOWLClassAssertionAxiom(Resource, Gearbox);
		ontology.add(GearboxResource);

		OWLIndividual S_Gearbox_oilTemp = factory.getOWLNamedIndividual(IRI.create(ns,"S_Gearbox_oilTemp"));
		OWLClassAssertionAxiom Sensor_Gearbox_oilTemp = factory.getOWLClassAssertionAxiom(Sensor, S_Gearbox_oilTemp);
		ontology.add(Sensor_Gearbox_oilTemp);
		
		OWLIndividual Gearbox_oilTemp = factory.getOWLNamedIndividual(IRI.create(ns,"Gearbox_oilTemp"));
		OWLClassAssertionAxiom Gearbox_oilTemp_Prop = factory.getOWLClassAssertionAxiom(ObservableProperty, Gearbox_oilTemp);
		ontology.add(Gearbox_oilTemp_Prop);
		
		OWLObjectPropertyAssertionAxiom Gearbox_SensorTemp = factory.getOWLObjectPropertyAssertionAxiom(hosts, Gearbox, S_Gearbox_oilTemp);
		ontology.add(Gearbox_SensorTemp);
		OWLObjectPropertyAssertionAxiom obsProp_GearboxOilTemp = factory.getOWLObjectPropertyAssertionAxiom(observedProperty, S_Gearbox_oilTemp, Gearbox_oilTemp);
		ontology.add(obsProp_GearboxOilTemp);

		OWLIndividual S_Gearbox_speed = factory.getOWLNamedIndividual(IRI.create(ns,"S_Gearbox_speed"));
		OWLClassAssertionAxiom Sensor_Gearbox_speed = factory.getOWLClassAssertionAxiom(Sensor, S_Gearbox_speed);
		ontology.add(Sensor_Gearbox_speed);
		
		OWLIndividual Gearbox_speed = factory.getOWLNamedIndividual(IRI.create(ns,"Gearbox_speed"));
		OWLClassAssertionAxiom Gearbox_speed_Prop = factory.getOWLClassAssertionAxiom(ObservableProperty, Gearbox_speed);
		ontology.add(Gearbox_speed_Prop);

		OWLObjectPropertyAssertionAxiom Gearbox_SensorSpeed = factory.getOWLObjectPropertyAssertionAxiom(hosts, Gearbox, S_Gearbox_speed);
		ontology.add(Gearbox_SensorSpeed);
		OWLObjectPropertyAssertionAxiom obsProp_GearboxSpeed = factory.getOWLObjectPropertyAssertionAxiom(observedProperty, S_Gearbox_speed, Gearbox_speed);
		ontology.add(obsProp_GearboxSpeed);
		
		OWLIndividual Converter = factory.getOWLNamedIndividual(IRI.create(ns,"Converter"));
		OWLClassAssertionAxiom ConverterResource = factory.getOWLClassAssertionAxiom(Resource, Converter);
		ontology.add(ConverterResource);
		
		OWLIndividual S_PowOutput_Converter = factory.getOWLNamedIndividual(IRI.create(ns,"S_PowOutput_Converter"));
		OWLClassAssertionAxiom Sensor_PowOutput_Converter = factory.getOWLClassAssertionAxiom(Sensor, S_PowOutput_Converter);
		ontology.add(Sensor_PowOutput_Converter);

		OWLIndividual PowOutput_Converter = factory.getOWLNamedIndividual(IRI.create(ns,"PowOutput_Converter"));
		OWLClassAssertionAxiom PowOutput_Converter_Prop = factory.getOWLClassAssertionAxiom(ObservableProperty, PowOutput_Converter);
		ontology.add(PowOutput_Converter_Prop);

		OWLObjectPropertyAssertionAxiom Converter_PowerOutput = factory.getOWLObjectPropertyAssertionAxiom(hosts, Converter, S_PowOutput_Converter);
		ontology.add(Converter_PowerOutput);
		OWLObjectPropertyAssertionAxiom obsProp_ConverterPowerOutput = factory.getOWLObjectPropertyAssertionAxiom(observedProperty, S_PowOutput_Converter, PowOutput_Converter);
		ontology.add(obsProp_ConverterPowerOutput);


		OWLClass Anomaly_GearOilTemp = factory.getOWLClass(IRI.create(nsSituation + "Anomaly_GearOilTemp"));

		OWLObjectHasValue observedProperty_Gearbox_oilTemp_Restriction = factory.getOWLObjectHasValue(observedProperty, Gearbox_oilTemp);
		OWLObjectHasValue madeIn_Gearbox = factory.getOWLObjectHasValue(madeIn, Gearbox);
		OWLDataRange dataRange = null;
		dataRange = factory.getOWLDatatypeMaxExclusiveRestriction(65);
		OWLDataSomeValuesFrom hasResult_Gearbox_oilTemp = factory.getOWLDataSomeValuesFrom(hasSimpleResult, dataRange);
		//OWLDataHasValue hasResult_Gearbox_oilTemp = factory.getOWLDataHasValue(hasSimpleResult, factory.getOWLLiteral(65));
		

		Set<OWLClassExpression> mySet = new HashSet<OWLClassExpression>();
		mySet.add(Observation);
		mySet.add(observedProperty_Gearbox_oilTemp_Restriction);
		mySet.add(madeIn_Gearbox);
		mySet.add(hasResult_Gearbox_oilTemp);
		OWLObjectIntersectionOf a = factory.getOWLObjectIntersectionOf(mySet);
		ontology.add(factory.getOWLSubClassOfAxiom(Anomaly_GearOilTemp,a));

		/*
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
		*/
        
		File fileformated = new File("/home/franco/Repositories/OntoInd4/test.owl");
        
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
	
        /* CONSISTENT CHECKING
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
        String answer;        
        if (reasoner.isConsistent()) answer = "Ouiiiii!!!"; else answer = "No";
        System.out.println ("The ontology is Consistent? " + answer);
         */
    }
}