
Jersey: A pluggable JavaScript execution environment
====================================================

This is a pluggable JavaScript execution environment based on Rhino. It can run on any Java runtime above Java 5.

Jersey also contains a package manager called jpm, so you just need to provide an ivy module file, then you can get all the plugin and plugin dependencies using maven distribution.

Concept
-------

Jersey is a running environment for JavaScript, it can run Mozilla Rhino flavoured JavaScript using Java. The aim for Jersey, is to provide a nice and pluggable environment to playwith or use JavaScript and based on the richi set of Java libraries.

The concept of Jersey is as list bellow:

* Pluggable: Jersey is pluggable, you can install any plugin to it, and the plugin is just a java jar. All the plugins are deactivate at the start of the enviromnent, you can activate the plugins using Spring(Jersey provides you the functions to do that). 
* Simple: Jersey try to provide the api as simple as possible, and provide a console to play JavaScript with and will add the support of other script that compiles to JavaScript(for example, CoffeeScript) using the console, and the console support the tab completion, so you can fool around very easily
* Configurable: Jersey provides the configuration basics based on Java's properties, and all the configuration for application and plugins contains within the same configuration file
* Discoverable: You can list all the provided function and provided modules using native functions.
* Docable: Jersey provides the doc method for all the function and plugin modules, you can read the documentation of the function and modules using man function, and even have a pager function to view the pages.
* Easy to be server: Since Java has many good embedded services, Jersey using Apache Derby to provide Database service and using Jetty to provide http server service, I also have a small CMS system written using the mvc and http plugin of Jersey
* Easy to use: Jersey providing the pacakge management system based on the Famous Maven and Ivy, so you can download every plugin and plugin dependencies using Ivy's module file.
* Can support other languages: Since JavaScript is not a very simple language to write, Jersey also supports Coffee script(detected by the file extention) and using coffee to compile it and run, will support others in the future
* Powerful: Jersey is based on Java, so it can take advantage of current Java and opensource Java libraries to provide powerful functions, it can use JDBC to access Database, using Commons DBCP to make the database connection pool, it can use Solr client to access the Apache Solr, it can use Log4J to handle the logging things and even using JBoss Drools to do the Rule matching.
* Stable: Thanks to Java's stablility and JavaScript's simplicity, Jersey can be very stable for running, it can run very long time without memory leaks or make system unstable. Jersey's multithreading is using Java's multithreading support, and Jersey provides a JobManager function using Java's concurrent library.

Architecture
------------

Jersey is based on [Java](http://www.java.com), [Rhino](https://developer.mozilla.org/en-US/docs/Rhino) and [Spring](http://www.springframework.org), the component structure of Jersey is based on Spring, and the interpreter of JavaScript is using Rhino.

All the native functions and native modules is initiliazed at the intializing of the environment, all the plugins you want to use, need to be installed into the lib folder first, configure it correctly in the configuration file, then using require function to require the intialize script for this plugin.

Usage
-----

Jersey is used using commandline, you can run the console using just jersey command. Or jersey a.js b.js c.js to run the script file one by one.

Jersey accept -c option to get the option file location (default is config.properties), and -s to get the script file(This only used for standard in, standard in file for Jersey is - )

Native Functions
----------------

Here is docs for some native functions that Jersey provided, you can list all the native functions using function functions().

* require: Require the library using the resouce location, support file:file and classpath: protocol.
* appContext: The function to load application context to javascript console
* config: Read the configuration from system.properties or list all the properties
* functions: List all the functions this shell provided.
* modules:Prints all the modules that have loaded.

Native Modules
--------------

Here is the docs for some native modules that Jersey provided, you can lis all the native modules using function modules().

* console: The console object.
* file: The file utils.
* csv: The csv utils service.
* sutils: The string utils service.

Standard Libraries
------------------

Jersey provides some standard JavaScript libraries in the distribution. Here is the list of the libraries:

* std/common: This provides the common extention of JavaScript, for example capitalize, isBlank, endWith for JavaScript and so on.
* std/date: This provides [date](http://www.datejs.com)
* std/evn.rhino: This provide [env](http://www.envjs.com), to use library like jQuery
* std/man: This provide the manual function for all the native function and modules
* std/jquery: This provides the famous [jQuery](http://www.jquery.com)
* std/underscore: This provides [underscore](http://underscorejs.org)

You can load your scripts using classpath protocol too.
