package com.accommodation.system;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * User: huongnq4
 * Date:  29/05/2020
 * Time: 15 :48
 * To change this template use File | Settings | File and Code Templates.
 */
public class GraphQLTest {
    public static void main(String[] args) {
//        String schema = "type Query{hello: String} schema{query: Query}";
//
//        SchemaParser schemaParser = new SchemaParser();
//        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);
//
////        RuntimeWiring runtimeWiring;
////        runtimeWiring = new RuntimeWiring()
////                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
////                .build();
//
//        SchemaGenerator schemaGenerator = new SchemaGenerator();
//        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
//
//        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
//        ExecutionResult executionResult = build.execute("{hello}");
//
//        System.out.println(executionResult.getData().toString());
    }
}
