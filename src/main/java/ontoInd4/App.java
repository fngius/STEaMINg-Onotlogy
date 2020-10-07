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
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
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
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class App {

	public static void main(String[] args) throws OWLOntologyCreationException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();

		String ontologyURI = "http://onto";
		String ns = ontologyURI + "#";

		OntologyAssistant oa = new OntologyAssistant();

		OWLOntology ontology = manager.createOntology(IRI.create(ontologyURI));

		String pre_GEO = "http://www.opengis.net/ont/geosparql#";
		String pre_TIME = "http://www.w3.org/2006/time#";
		String pre_SOSAOnt = "http://www.w3.org/ns/sosa/";
		String pre_SSNOnt = "http://www.w3.org/ns/ssn/";

		// OWLDatatype floatDatatype = factory.getFloatOWLDatatype();
		// OWLDatatype doubleDatatype = factory.getDoubleOWLDatatype();
		// OWLDatatype booleanDatatype = factory.getBooleanOWLDatatype();
		// OWLDatatype integerDatatype = factory.getIntegerOWLDatatype();
		OWLDatatype doubleDatatype = factory.getDoubleOWLDatatype();
		OWLDatatype dateTimeStamp = factory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTimeStamp"));
		OWLDatatype wkt = factory.getOWLDatatype(pre_GEO + "wktLiteral");
		OWLDatatype gml = factory.getOWLDatatype(pre_GEO + "gmlLiteral");

		/*
		 * IMPORT SSN, TIME and GEOSPARQL Ontologies
		 * 
		 * OWLImportsDeclaration importDeclarationGEO =
		 * factory.getOWLImportsDeclaration(IRI.create(pre_GEO)); OWLImportsDeclaration
		 * importDeclarationTIME =
		 * factory.getOWLImportsDeclaration(IRI.create(pre_TIME)); OWLImportsDeclaration
		 * importDeclarationSSN =
		 * factory.getOWLImportsDeclaration(IRI.create(pre_SSNOnt));
		 * manager.applyChange(new AddImport(ontology, importDeclarationGEO));
		 * manager.applyChange(new AddImport(ontology, importDeclarationTIME));
		 * manager.applyChange(new AddImport(ontology, importDeclarationSSN));
		 */


		/* PROCESS module */

		OWLClass Process = factory.getOWLClass(IRI.create(ns, "Process"));
		OWLClass ManufacturingProcess = factory.getOWLClass(IRI.create(ns, "ManufacturingProcess"));
		OWLClass LogisticProcess = factory.getOWLClass(IRI.create(ns, "LogisticProcess"));
		OWLClass HumanProcess = factory.getOWLClass(IRI.create(ns, "HumanProcess"));

		//ontology.add(factory.getOWLDeclarationAxiom(Process));
		ontology.add(factory.getOWLSubClassOfAxiom(ManufacturingProcess, Process));
		ontology.add(factory.getOWLSubClassOfAxiom(LogisticProcess, Process));
		ontology.add(factory.getOWLSubClassOfAxiom(HumanProcess, Process));

		/* RESOURCE module */

		OWLClass Resource = factory.getOWLClass(IRI.create(ns, "Resource"));
		OWLClass Product = factory.getOWLClass(IRI.create(ns, "Product"));
		OWLClass Part = factory.getOWLClass(IRI.create(ns, "Part"));
		OWLClass Staff = factory.getOWLClass(IRI.create(ns, "Staff"));
		OWLClass Operator = factory.getOWLClass(IRI.create(ns, "Operator"));
		OWLClass Technician = factory.getOWLClass(IRI.create(ns, "Technician"));
		OWLClass Manager = factory.getOWLClass(IRI.create(ns, "Manager"));
		OWLClass ManufacturingFacility = factory.getOWLClass(IRI.create(ns, "ManufacturingFacility"));
		OWLClass Line = factory.getOWLClass(IRI.create(ns, "Line"));
		OWLClass Cell = factory.getOWLClass(IRI.create(ns, "Cell"));
		OWLClass WorkStation = factory.getOWLClass(IRI.create(ns, "WorkStation"));
		OWLClass Machine = factory.getOWLClass(IRI.create(ns, "Machine"));

		ontology.add(factory.getOWLDeclarationAxiom(Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Product, Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Part, Product));
		ontology.add(factory.getOWLSubClassOfAxiom(Staff, Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Operator, Staff));
		ontology.add(factory.getOWLSubClassOfAxiom(Technician, Staff));
		ontology.add(factory.getOWLSubClassOfAxiom(Manager, Staff));
		ontology.add(factory.getOWLSubClassOfAxiom(ManufacturingFacility, Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Line, ManufacturingFacility));
		ontology.add(factory.getOWLSubClassOfAxiom(Cell, ManufacturingFacility));
		ontology.add(factory.getOWLSubClassOfAxiom(WorkStation, ManufacturingFacility));
		ontology.add(factory.getOWLSubClassOfAxiom(Machine, ManufacturingFacility));

		List<OWLClass> list = Arrays.asList(Machine, WorkStation, Cell, Line);
		ontology.add(factory.getOWLDisjointClassesAxiom(list));

		OWLObjectProperty isPartOf = factory.getOWLObjectProperty(IRI.create(ns, "isPartOf"));
		OWLObjectPropertyDomainAxiom isPartOfdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(isPartOf,
				ManufacturingFacility);
		OWLObjectPropertyRangeAxiom isPartOfrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(isPartOf,
				ManufacturingFacility);
		ontology.add(factory.getOWLDeclarationAxiom(isPartOf));
		ontology.add(isPartOfdomainAxiom);
		ontology.add(isPartOfrangeAxiom);

		OWLObjectProperty operates = factory.getOWLObjectProperty(IRI.create(ns, "operates"));
		OWLObjectPropertyDomainAxiom operatesdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(operates, Staff);
		OWLObjectPropertyRangeAxiom operatesrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(operates,
				ManufacturingFacility);
		ontology.add(factory.getOWLDeclarationAxiom(operates));
		ontology.add(operatesdomainAxiom);
		ontology.add(operatesrangeAxiom);


		/* SITUATION module */
		
		OWLClass Situation = factory.getOWLClass(IRI.create(ns, "Situation"));
		OWLClass Cause = factory.getOWLClass(IRI.create(ns, "Cause"));
		OWLClass Constraint = factory.getOWLClass(IRI.create(ns, "Constraint"));
		OWLClass Action = factory.getOWLClass(IRI.create(ns, "Action"));
		ontology.add(factory.getOWLDeclarationAxiom(Situation));
		ontology.add(factory.getOWLDeclarationAxiom(Cause));
		ontology.add(factory.getOWLDeclarationAxiom(Constraint));
		ontology.add(factory.getOWLDeclarationAxiom(Action));
		
		OWLObjectProperty hasCause = factory.getOWLObjectProperty(IRI.create(ns, "hasCause"));		
		OWLObjectPropertyDomainAxiom hasCausedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasCause, Situation);
		OWLObjectPropertyRangeAxiom hasCauserangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasCause, Cause);
		ontology.add(factory.getOWLDeclarationAxiom(hasCause));
		ontology.add(hasCausedomainAxiom);
		ontology.add(hasCauserangeAxiom);

		OWLObjectProperty requiresAction = factory.getOWLObjectProperty(IRI.create(ns, "requiresAction"));		
		OWLObjectPropertyDomainAxiom requiresActiondomainAxiom = factory.getOWLObjectPropertyDomainAxiom(requiresAction, Situation);
		OWLObjectPropertyRangeAxiom requiresActionrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(requiresAction, Action);
		ontology.add(factory.getOWLDeclarationAxiom(requiresAction));
		ontology.add(requiresActiondomainAxiom);
		ontology.add(requiresActionrangeAxiom);

		OWLObjectProperty associatedConstraint = factory.getOWLObjectProperty(IRI.create(ns, "associatedConstraint"));		
		OWLObjectPropertyDomainAxiom associatedConstraintdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(associatedConstraint, Situation);
		OWLObjectPropertyRangeAxiom associatedConstraintrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(associatedConstraint, Constraint);
		ontology.add(factory.getOWLDeclarationAxiom(associatedConstraint));
		ontology.add(associatedConstraintdomainAxiom);
		ontology.add(associatedConstraintrangeAxiom);

		/* SENSOR module */

		OWLClass Platform = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Platform"));
		OWLClass Sensor = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Sensor"));
		OWLClass Property = factory.getOWLClass(IRI.create(pre_SSNOnt + "Property"));
		OWLClass ObservableProperty = factory.getOWLClass(IRI.create(pre_SOSAOnt + "ObservableProperty"));
		OWLClass Observation = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Observation"));
		OWLClass FeatureOfInterest = factory.getOWLClass(IRI.create(pre_SOSAOnt + "FeatureOfInterest"));
		OWLClass Result = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Result"));

		//ontology.add(factory.getOWLDeclarationAxiom(Platform));
		ontology.add(factory.getOWLDeclarationAxiom(Result));
		ontology.add(factory.getOWLDeclarationAxiom(Sensor));
		ontology.add(factory.getOWLDeclarationAxiom(Property));
		ontology.add(factory.getOWLSubClassOfAxiom(ObservableProperty, Property));
		ontology.add(factory.getOWLDeclarationAxiom(Observation));
		ontology.add(factory.getOWLDeclarationAxiom(FeatureOfInterest));

		ontology.add(factory.getOWLEquivalentClassesAxiom(Resource, Platform));

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
		OWLObjectPropertyDomainAxiom observedPropertydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(observedProperty,
				Observation);
		OWLObjectPropertyRangeAxiom observedPropertyrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(observedProperty,
				ObservableProperty);
		ontology.add(factory.getOWLDeclarationAxiom(observedProperty));
		ontology.add(observedPropertydomainAxiom);
		ontology.add(observedPropertyrangeAxiom);

		OWLObjectProperty hasFeatureOfInterest = factory
				.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "hasFeatureOfInterest"));
		OWLObjectPropertyDomainAxiom hasFeatureOfInterestdomainAxiom = factory
				.getOWLObjectPropertyDomainAxiom(hasFeatureOfInterest, Observation);
		OWLObjectPropertyRangeAxiom hasFeatureOfInterestrangeAxiom = factory
				.getOWLObjectPropertyRangeAxiom(hasFeatureOfInterest, FeatureOfInterest);
		ontology.add(factory.getOWLDeclarationAxiom(observedProperty));
		ontology.add(hasFeatureOfInterestdomainAxiom);
		ontology.add(hasFeatureOfInterestrangeAxiom);

		OWLObjectProperty hasProperty = factory.getOWLObjectProperty(IRI.create(pre_SSNOnt + "hasProperty"));
		OWLObjectPropertyDomainAxiom hasPropertydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasProperty,
				FeatureOfInterest);
		OWLObjectPropertyRangeAxiom hasPropertyrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasProperty, Property);
		ontology.add(factory.getOWLDeclarationAxiom(hasProperty));
		ontology.add(hasPropertydomainAxiom);
		ontology.add(hasPropertyrangeAxiom);

		OWLDataProperty hasSimpleResult = factory.getOWLDataProperty(IRI.create(pre_SOSAOnt + "hasSimpleResult"));
		OWLDataPropertyDomainAxiom hasSimpleResultdomainAxiom = factory.getOWLDataPropertyDomainAxiom(hasSimpleResult,
				Observation);
		OWLDataPropertyRangeAxiom hasSimpleResultrangeAxiom = factory.getOWLDataPropertyRangeAxiom(hasSimpleResult,
				doubleDatatype);
		ontology.add(factory.getOWLDeclarationAxiom(hasSimpleResult));
		ontology.add(hasSimpleResultdomainAxiom);
		ontology.add(hasSimpleResultrangeAxiom);


		/* TIME module */

		OWLClass TemporalEntity = factory.getOWLClass(IRI.create(pre_TIME + "TemporalEntity"));
		OWLClass Instant = factory.getOWLClass(IRI.create(pre_TIME + "Instant"));
		OWLClass Interval = factory.getOWLClass(IRI.create(pre_TIME + "Interval"));
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
		OWLObjectPropertyDomainAxiom intervalDisjointdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalDisjoint,
				Interval);
		OWLObjectPropertyRangeAxiom intervalDisjointrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalDisjoint,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalDisjoint));
		ontology.add(intervalDisjointdomainAxiom);
		ontology.add(intervalDisjointrangeAxiom);

		OWLObjectProperty intervalEquals = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalEquals"));
		OWLObjectPropertyDomainAxiom intervalEqualsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalEquals,
				Interval);
		OWLObjectPropertyRangeAxiom intervalEqualsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalEquals,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalEquals));
		ontology.add(intervalEqualsdomainAxiom);
		ontology.add(intervalEqualsrangeAxiom);

		OWLObjectProperty intervalFinishedBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalFinishedBy"));
		OWLObjectPropertyDomainAxiom intervalFinishedBydomainAxiom = factory
				.getOWLObjectPropertyDomainAxiom(intervalFinishedBy, Interval);
		OWLObjectPropertyRangeAxiom intervalFinishedByrangeAxiom = factory
				.getOWLObjectPropertyRangeAxiom(intervalFinishedBy, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalFinishedBy));
		ontology.add(intervalFinishedBydomainAxiom);
		ontology.add(intervalFinishedByrangeAxiom);

		OWLObjectProperty intervalFinishes = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalFinishes"));
		OWLObjectPropertyDomainAxiom intervalFinishesdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalFinishes,
				Interval);
		OWLObjectPropertyRangeAxiom intervalFinishesrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalFinishes,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalFinishes));
		ontology.add(intervalFinishesdomainAxiom);
		ontology.add(intervalFinishesrangeAxiom);

		OWLObjectProperty intervalContains = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalContains"));
		OWLObjectPropertyDomainAxiom intervalContainsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalContains,
				Interval);
		OWLObjectPropertyRangeAxiom intervalContainsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalContains,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalContains));
		ontology.add(intervalContainsdomainAxiom);
		ontology.add(intervalContainsrangeAxiom);

		OWLObjectProperty intervalDuring = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalDuring"));
		OWLObjectPropertyDomainAxiom intervalDuringdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalDuring,
				Interval);
		OWLObjectPropertyRangeAxiom intervalDuringrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalDuring,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalDuring));
		ontology.add(intervalDuringdomainAxiom);
		ontology.add(intervalDuringrangeAxiom);

		OWLObjectProperty intervalStartedBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalStartedBy"));
		OWLObjectPropertyDomainAxiom intervalStartedBydomainAxiom = factory
				.getOWLObjectPropertyDomainAxiom(intervalStartedBy, Interval);
		OWLObjectPropertyRangeAxiom intervalStartedByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalStartedBy,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalStartedBy));
		ontology.add(intervalStartedBydomainAxiom);
		ontology.add(intervalStartedByrangeAxiom);

		OWLObjectProperty intervalStarts = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalStarts"));
		OWLObjectPropertyDomainAxiom intervalStartsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalStarts,
				Interval);
		OWLObjectPropertyRangeAxiom intervalStartsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalStarts,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalStarts));
		ontology.add(intervalStartsdomainAxiom);
		ontology.add(intervalStartsrangeAxiom);

		OWLObjectProperty intervalOverlappedBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalOverlaps"));
		OWLObjectPropertyDomainAxiom intervalOverlappedBydomainAxiom = factory
				.getOWLObjectPropertyDomainAxiom(intervalOverlappedBy, Interval);
		OWLObjectPropertyRangeAxiom intervalOverlappedByrangeAxiom = factory
				.getOWLObjectPropertyRangeAxiom(intervalOverlappedBy, Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalOverlappedBy));
		ontology.add(intervalOverlappedBydomainAxiom);
		ontology.add(intervalOverlappedByrangeAxiom);

		OWLObjectProperty intervalOverlaps = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalOverlaps"));
		OWLObjectPropertyDomainAxiom intervalOverlapsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalOverlaps,
				Interval);
		OWLObjectPropertyRangeAxiom intervalOverlapsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalOverlaps,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalOverlaps));
		ontology.add(intervalOverlapsdomainAxiom);
		ontology.add(intervalOverlapsrangeAxiom);

		OWLObjectProperty intervalMetBy = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalMetBy"));
		OWLObjectPropertyDomainAxiom intervalMetBydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalMetBy,
				Interval);
		OWLObjectPropertyRangeAxiom intervalMetByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalMetBy,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalMetBy));
		ontology.add(intervalMetBydomainAxiom);
		ontology.add(intervalMetByrangeAxiom);

		OWLObjectProperty intervalMeets = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalMeets"));
		OWLObjectPropertyDomainAxiom intervalMeetsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalMeets,
				Interval);
		OWLObjectPropertyRangeAxiom intervalMeetsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalMeets,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalMeets));
		ontology.add(intervalMeetsdomainAxiom);
		ontology.add(intervalMeetsrangeAxiom);

		OWLObjectProperty intervalAfter = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalAfter"));
		OWLObjectPropertyDomainAxiom intervalAfterdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalAfter,
				Interval);
		OWLObjectPropertyRangeAxiom intervalAfterrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalAfter,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalAfter));
		ontology.add(intervalAfterdomainAxiom);
		ontology.add(intervalAfterrangeAxiom);

		OWLObjectProperty intervalBefore = factory.getOWLObjectProperty(IRI.create(pre_TIME + "intervalBefore"));
		OWLObjectPropertyDomainAxiom intervalBeforedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(intervalBefore,
				Interval);
		OWLObjectPropertyRangeAxiom intervalBeforerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(intervalBefore,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(intervalBefore));
		ontology.add(intervalBeforedomainAxiom);
		ontology.add(intervalBeforerangeAxiom);

		OWLObjectProperty hasBeginning = factory.getOWLObjectProperty(IRI.create(pre_TIME + "hasBeginning"));
		OWLObjectPropertyDomainAxiom hasBeginningdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasBeginning,
				TemporalEntity);
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
		OWLDataPropertyDomainAxiom inXSDDateTimeStampdomainAxiom = factory.getOWLDataPropertyDomainAxiom(inXSDDateTimeStamp,
				Instant);
		OWLDataPropertyRangeAxiom inXSDDateTimeStamprangeAxiom = factory.getOWLDataPropertyRangeAxiom(inXSDDateTimeStamp,
				dateTimeStamp);
		ontology.add(factory.getOWLDeclarationAxiom(inXSDDateTimeStamp));
		ontology.add(inXSDDateTimeStampdomainAxiom);
		ontology.add(inXSDDateTimeStamprangeAxiom);

		/* LOCATION module */
		
		OWLClass SpatialObject = factory.getOWLClass(IRI.create(pre_GEO + "SpatialObject"));
		OWLClass Feature = factory.getOWLClass(IRI.create(pre_GEO + "Feature"));
		OWLClass Geometry = factory.getOWLClass(IRI.create(pre_GEO + "Geometry"));
		ontology.add(factory.getOWLDeclarationAxiom(SpatialObject));
		ontology.add(factory.getOWLSubClassOfAxiom(Feature, SpatialObject));
		ontology.add(factory.getOWLSubClassOfAxiom(Geometry, SpatialObject));

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
		OWLObjectPropertyDomainAxiom rcc8ntppidomainAxiom = factory.getOWLObjectPropertyDomainAxiom(rcc8ntppi,
				SpatialObject);
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


		/* ALIGNEMENTS AMONG MODULES */

		ontology.add(factory.getOWLSubClassOfAxiom(Resource, Feature));
		ontology.add(factory.getOWLSubClassOfAxiom(Platform, Feature));

		
		/* RESOURCE module with the others */

		OWLObjectProperty hosts = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "hosts"));
		OWLObjectPropertyDomainAxiom hostsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hosts, Platform);
		OWLObjectPropertyRangeAxiom hostsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hosts, Sensor);
		ontology.add(factory.getOWLDeclarationAxiom(hosts));
		ontology.add(hostsdomainAxiom);
		ontology.add(hostsrangeAxiom);

		OWLObjectProperty isInlocation = factory.getOWLObjectProperty(IRI.create(ns, "isInlocation"));
		OWLObjectPropertyDomainAxiom isInlocationdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(isInlocation,
				Resource);
		OWLObjectPropertyRangeAxiom isInlocationrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(isInlocation,
				SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(isInlocation));
		ontology.add(isInlocationdomainAxiom);
		ontology.add(isInlocationrangeAxiom);

		OWLObjectProperty performsPorcess = factory.getOWLObjectProperty(IRI.create(ns, "performsPorcess"));
		OWLObjectPropertyDomainAxiom performsPorcessdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(performsPorcess, Resource);
		OWLObjectPropertyRangeAxiom performsPorcessrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(performsPorcess, Process);
		ontology.add(factory.getOWLDeclarationAxiom(performsPorcess));
		ontology.add(performsPorcessdomainAxiom);
		ontology.add(performsPorcessrangeAxiom);

		OWLObjectProperty concernBy = factory.getOWLObjectProperty(IRI.create(ns, "concernBy"));
		OWLObjectPropertyDomainAxiom concernBydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(concernBy, Resource);
		OWLObjectPropertyRangeAxiom concernByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(concernBy, Situation);
		ontology.add(factory.getOWLDeclarationAxiom(concernBy));
		ontology.add(concernBydomainAxiom);
		ontology.add(concernByrangeAxiom);

		/* SENSOR module with others */

		OWLObjectProperty locatedIn = factory.getOWLObjectProperty(IRI.create(ns, "locatedIn"));
		OWLObjectPropertyDomainAxiom locatedIndomainAxiom = factory.getOWLObjectPropertyDomainAxiom(locatedIn,
		Sensor);
		OWLObjectPropertyRangeAxiom locatedInrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(locatedIn, SpatialObject);
		ontology.add(factory.getOWLDeclarationAxiom(locatedIn));
		ontology.add(locatedIndomainAxiom);
		ontology.add(locatedInrangeAxiom);

		OWLObjectProperty hasTime = factory.getOWLObjectProperty(IRI.create(ns, "hasTime"));
		OWLObjectPropertyDomainAxiom hasTimedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasTime, Observation);
		OWLObjectPropertyRangeAxiom hasTimerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasTime, TemporalEntity);
		ontology.add(factory.getOWLDeclarationAxiom(hasTime));
		ontology.add(hasTimedomainAxiom);
		ontology.add(hasTimerangeAxiom);

		/* PROCESS module with others */

		OWLObjectProperty hasDuration = factory.getOWLObjectProperty(IRI.create(ns, "hasDuration"));
		OWLObjectPropertyDomainAxiom hasDurationdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasDuration, Process);
		OWLObjectPropertyRangeAxiom hasDurationrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasDuration,
				TemporalEntity);
		ontology.add(factory.getOWLDeclarationAxiom(hasDuration));
		ontology.add(hasDurationdomainAxiom);
		ontology.add(hasDurationrangeAxiom);

		/* SITUATION module with others */
		
		OWLObjectProperty isInSituation = factory.getOWLObjectProperty(IRI.create(ns + "isInSituation"));
		OWLObjectPropertyDomainAxiom isInSituationdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(isInSituation,
				Observation);
		OWLObjectPropertyRangeAxiom isInSituationrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(isInSituation,
				Situation);
		ontology.add(factory.getOWLDeclarationAxiom(isInSituation));
		ontology.add(isInSituationdomainAxiom);
		ontology.add(isInSituationrangeAxiom);
		
		OWLObjectProperty hasObservation = factory.getOWLObjectProperty(IRI.create(ns + "hasObservation"));
		OWLObjectPropertyDomainAxiom hasObservationdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasObservation,
				Situation);
		OWLObjectPropertyRangeAxiom hasObservationrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasObservation,
				Observation);
		ontology.add(factory.getOWLDeclarationAxiom(hasObservation));
		ontology.add(hasObservationdomainAxiom);
		ontology.add(hasObservationrangeAxiom);

		OWLObjectProperty definedOn = factory.getOWLObjectProperty(IRI.create(ns + "definedOn"));
		OWLObjectPropertyDomainAxiom definedOndomainAxiom = factory.getOWLObjectPropertyDomainAxiom(definedOn,
				Constraint);
		OWLObjectPropertyRangeAxiom definedOnrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(definedOn,
				ObservableProperty);
		ontology.add(factory.getOWLDeclarationAxiom(definedOn));
		ontology.add(definedOndomainAxiom);
		ontology.add(definedOnrangeAxiom);

		OWLObjectProperty situationTime = factory.getOWLObjectProperty(IRI.create(ns + "situationTime"));
		OWLObjectPropertyDomainAxiom situationTimedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(situationTime,
				Situation);
		OWLObjectPropertyRangeAxiom situationTimerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(situationTime,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(situationTime));
		ontology.add(situationTimedomainAxiom);
		ontology.add(situationTimerangeAxiom);

		/* INDIVIDUALS */
		OWLIndividual PL1 = factory.getOWLNamedIndividual(IRI.create(ns,"PL1"));
		OWLClassAssertionAxiom prodLinePL1 = factory.getOWLClassAssertionAxiom(Line, PL1);
		ontology.add(prodLinePL1);

		OWLIndividual M3 = factory.getOWLNamedIndividual(IRI.create(ns,"M3"));
		OWLClassAssertionAxiom machineM3 = factory.getOWLClassAssertionAxiom(Machine, M3);
		ontology.add(machineM3);

		oa.relateIndividuals(ontology, manager, factory, isPartOf, M3, PL1);

		OWLIndividual S_C_Wtemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_C_Wtemp"));
		OWLClassAssertionAxiom sensorS_C_Wtemp = factory.getOWLClassAssertionAxiom(Sensor, S_C_Wtemp);
		ontology.add(sensorS_C_Wtemp);
		OWLIndividual S_TG_temp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_TG_temp"));
		OWLClassAssertionAxiom sensorS_TG_temp = factory.getOWLClassAssertionAxiom(Sensor, S_TG_temp);
		ontology.add(sensorS_TG_temp);
		OWLIndividual S_G_temp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_G_temp"));
		OWLClassAssertionAxiom sensorS_G_temp = factory.getOWLClassAssertionAxiom(Sensor, S_G_temp);
		ontology.add(sensorS_G_temp);

		oa.relateIndividuals(ontology, manager, factory, hosts, M3, S_C_Wtemp);
		oa.relateIndividuals(ontology, manager, factory, hosts, M3, S_TG_temp);
		oa.relateIndividuals(ontology, manager, factory, hosts, M3, S_G_temp);

		File fileformated = new File("/home/franco/Repositories/OntoInd4/NEWONTOLOGY.owl");

		try {
			// ontology.saveOntology(System.out);
			ontology.saveOntology(IRI.create(fileformated.toURI()));
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
		}

		/*
		 * CONSISTENT CHECKING 
		 */ 

		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); 
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor(); 
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor); 
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS); 
		
		if (reasoner.isConsistent()) 
			System.out.println ("The ontology is Consistent.");
		else 
			System.out.println ("ERROR");

	}
}