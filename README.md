

Usage 
-

#### Parse request into POGO object

    // curl -XPOST localhost:5050/person --data "age=18"  
    
    class Person {
        int age
    }
    
    chain.post("person", { Context ctx ->
        ctx.parse(Form) { Form form -> 
            Person person = form as Person
            assert person.age == 18
        }
    })

#### Parse request into Java Object

    // curl -XPOST localhost:5050/atomicinteger --data "value=42"
    
    import java.util.concurrent.atomic.AtomicInteger
    chain.post("atomicinteger", { Context ctx ->
        ctx.parse(Form) { Form form -> 
            AtomicInteger atomicInteger = form as AtomicInteger
            assert atomicInteger.get() == 42
        }
    })

#### Parse using custom form property name

    // curl -XPOST localhost:5050/balloon --data "mass_density=0.179"  
    
    import ratpack.form.FormProperty
    class Balloon {
        @FormProperty("mass_density")
        float massDensity
    }
    
    chain.post("balloon", { Context ctx ->
        ctx.parse(Form) { Form form -> 
            Balloon balloon = form as Balloon
            assert balloon.massDesnity == 0.179
        }
    })

#### Parse via set interception

    // curl -XPOST localhost:5050/book \ 
    // --data "author=Edgar Allan Poe" \
    // --data "stories=The Gold-Bug, The Murders in the Rue Morgue, The Mystery of Marie Roget"  
    
    class Book {
        String author
        List<String> stories
        def setStories(String commaSeparatedList) {
            stories = commaSeparatedList.split(",")*.trim()
        }
    }
    
    chain.post("book", { Context ctx ->
        ctx.parse(Form) { Form form -> 
            Book book = form as Book
            assert book.author == 'Edgar Allan Poe'
            assert book.stories == [
                'The Gold-Bug',
                'The Murders in the Rue Morgue', 
                'The Mystery of Marie Roget'
            ]
        }
    })
    
#### Validation

To use validation features, you must include one of the libraries that implement JSR 303  

    // curl -XPOST localhost:5050/person --data "age=15"
    
    class Person {
        @Min(value = 18, message = "You must be 18 years or older") 
        int age
    }
    
    chain.post("person", { Context ctx ->
        ctx.parse(Form) { Form form ->
            try {
                Person person = form as Person
            } catch(ValidationException e) {
                ctx.render e.message
            }
        }
    })
    
#### See also 

[FormIgnoreProperties](src/test/groovy/ratpack/groovy/extension/FormIgnorePropertiesTest.groovy)

