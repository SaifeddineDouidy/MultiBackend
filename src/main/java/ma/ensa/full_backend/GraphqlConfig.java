package ma.ensa.full_backend;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDate;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

@Configuration
public class GraphqlConfig {

    @Bean
    public GraphQLScalarType localDateScalar() {
        return GraphQLScalarType.newScalar()
                .name("LocalDate")
                .description("Java 8 LocalDate")
                .coercing(new Coercing<LocalDate, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof LocalDate) {
                            return dataFetcherResult.toString();  // e.g. "2024-12-10"
                        }
                        throw new CoercingSerializeException("Invalid LocalDate value");
                    }

                    @Override
                    public LocalDate parseValue(Object input) throws CoercingParseValueException {
                        if (input == null) {
                            throw new CoercingParseValueException("Invalid value: null for LocalDate");
                        }
                        try {
                            return LocalDate.parse(input.toString());  // Converts string to LocalDate
                        } catch (Exception e) {
                            throw new CoercingParseValueException("Invalid LocalDate value: " + input);
                        }
                    }

                    @Override
                    public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input == null) {
                            throw new CoercingParseLiteralException("Invalid literal: null for LocalDate");
                        }
                        try {
                            return LocalDate.parse(input.toString());  // Converts string to LocalDate
                        } catch (Exception e) {
                            throw new CoercingParseLiteralException("Invalid LocalDate literal: " + input);
                        }
                    }
                })
                .build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(localDateScalar())
                .scalar(ExtendedScalars.GraphQLLong);
    }
}
