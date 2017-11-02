package eu.supersede.mdm.storage.model.bdi_ontology.metamodel;

import eu.supersede.mdm.storage.model.bdi_ontology.Namespaces;

/**
 * Created by snadal on 22/11/16.
 */
public enum Rules {

    RULE(Namespaces.R.val()+"Rule/"),
    HAS_WINDOW(Namespaces.R.val()+"hasWindow/"),
    HAS_ACTION(Namespaces.R.val()+"hasAction/"),
    HAS_CEP_ELEMENT(Namespaces.R.val()+"hasCEPElement/"),
    HAS_CONDITION(Namespaces.R.val()+"hasCondition/"),
    HAS_FILTER(Namespaces.R.val()+"hasFilter/"),

    WINDOW(Namespaces.R.val()+"Window/"),
    HAS_WINDOW_ATTRIBUTE(Namespaces.R.val()+"hasWindowAttribute/"),

    WINDOW_ATTRIBUTE(Namespaces.R.val()+"WindowAttribute/"),
    WINDOW_KIND(Namespaces.R.val()+"WindowKind/"),
    SLIDING_WINDOW(Namespaces.R.val()+"SlidingWindow/"),
    TUMBLING_WINDOW(Namespaces.R.val()+"TumblingWindow/"),
    WINDOW_UNIT(Namespaces.R.val()+"WindowUnit/"),
    TIME_UNIT(Namespaces.R.val()+"TimeUnit/"),
    EVENT_UNIT(Namespaces.R.val()+"EventUnit/"),

    ACTION(Namespaces.R.val()+"Action/"),
    HAS_EVENT(Namespaces.R.val()+"hasEvent/"),
    HAS_ACTION_PARAMETER(Namespaces.R.val()+"hasActionParameter/"),

    ACTION_PARAMETER(Namespaces.R.val()+"ActionParameter/"),
    REPRESENTED_WITH(Namespaces.R.val()+"representedWith/"),
    HAS_ACTION_PARAMETER_ORDER(Namespaces.R.val()+"hasActionParameterOrder/"),

    EVENT(Namespaces.R.val()+"Event/"),
    HAS_EVENT_SCHEMA(Namespaces.R.val()+"hasEventSchema/"),
    HAS_EVENT_ATTRIBUTE(Namespaces.R.val()+"hasEventAttribute/"),

    EVENT_SCHEMA(Namespaces.R.val()+"EventSchema/"),

    EVENT_ATTRIBUTE(Namespaces.R.val()+"EventAttribute/"),

    PATTERN(Namespaces.R.val()+"Pattern/"),
    CONTAINS_ELEMENT(Namespaces.R.val()+"containsElement/"),
    TEMPORAL_PATTERN(Namespaces.R.val()+"TemporalPattern/"),
    USES_TEMPORAL_OPERATOR(Namespaces.R.val()+"usesTemporalOperator/"),
    LOGIC_PATTERN(Namespaces.R.val()+"LogicPattern/"),
    USES_LOGIC_OPERATOR(Namespaces.R.val()+"usesLogicOperator/"),

    OPERATOR(Namespaces.R.val()+"Operator/"),
    TEMPORTAL_OPERATOR(Namespaces.R.val()+"TemporalOperator/"),
    WITHIN(Namespaces.R.val()+"WithIn/"),
    HAS_OFFSET(Namespaces.R.val()+"hasOffset/"),
    OPERATOR_WITH_IMPLICIT_TIME(Namespaces.R.val()+"OperatorWithImplicitTime/"),
    SEQUENCE(Namespaces.R.val()+"Sequence/"),
    LOGIC_OPERATOR(Namespaces.R.val()+"LogicOperator/"),
    CONJUNCTION(Namespaces.R.val()+"Conjunction/"),
    DISJUNCTION(Namespaces.R.val()+"Disjunction/"),
    NEGATION(Namespaces.R.val()+"Negation/"),
    EQUAL(Namespaces.R.val()+"Equal/"),
    NOT_EQUAL(Namespaces.R.val()+"NotEqual/"),
    GREATER_THAN(Namespaces.R.val()+"GreaterThan/"),
    GREATER_OR_EQUAL(Namespaces.R.val()+"GreaterOrEqual/"),
    LESS_OR_EQUAL(Namespaces.R.val()+"LessOrEqual/"),
    LESS_THAN(Namespaces.R.val()+"LessThan/"),

    INCLUDED_ELEMENT(Namespaces.R.val()+"IncludedElement"),
    REPRESENTS_ELEMENT(Namespaces.R.val()+"representsElement"),
    HAS_ELEMENT_ORDER(Namespaces.R.val()+"hasElementOrder/"),


    CONDITION(Namespaces.R.val()+"Condition/"),

    SIMPLE_CLAUSE(Namespaces.R.val()+"SimpleClause/"),

    VALUE(Namespaces.R.val()+"Value/"),
    HAS_VALUE(Namespaces.R.val()+"hasValue/"),
    HAS_FEATURE(Namespaces.R.val()+"hasFeature/"),
    PREDICATE(Namespaces.R.val()+"Predicate/"),
    HAS_PREDICATE(Namespaces.R.val()+"hasPredicate/");


    private String element;

    Rules(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }
}
