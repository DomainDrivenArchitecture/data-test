# data-test - project has moved to federated gitea: https://repo.prod.meissa.de/meissa/data-test

[![Clojars Project](https://img.shields.io/clojars/v/dda/data-test.svg)](https://clojars.org/dda/data-test)
[![Build Status](https://travis-ci.org/DomainDrivenArchitecture/data-test.svg?branch=master)](https://travis-ci.org/DomainDrivenArchitecture/data-test)

[![Slack](https://img.shields.io/badge/chat-clojurians-green.svg?style=flat)](https://clojurians.slack.com/messages/#dda-pallet/) | [<img src="https://meissa-gmbh.de/img/community/Mastodon_Logotype.svg" width=20 alt="team@social.meissa-gmbh.de"> team@social.meissa-gmbh.de](https://social.meissa-gmbh.de/@team) | [Website & Blog](https://domaindrivenarchitecture.org)

## About

data-test separates test data from test code and allows a more data driven approach for testing. In case of having huge amounts of test-input & -expectations your test code will remain readable and concise. data-test is founded on and compatible with `clojure.test`. Integration in your test environments will work without any changes. For explicit, intentful and obvious data, data-test uses [aero](https://github.com/juxt/aero).

## Usage

Import data-test to your project

```clojure
[dda/data-test "0.1.1"]
```

Define your data test similar to `deftest` and express your test e.g. with `is` macro. The given binding `[input expected]` will let symbols which can be used in your test code.

```clojure
(ns my.cool.project-test
  (:require [clojure.test :refer :all]
            [data-test :refer :all]))

(defdatatest should-test-multiple-specs [input expected]
  (is (= input expected)))
```

Your test data are loaded as resource and therefore data has to be located in classpath. The path is determined by your tests namespace and your tests definition name. In given case it is `my/cool/project_test/should_test_multiple_spec.edn`:

```clojure
{:input 1
 :expected 1}
```

The keys `:input` and `:expected` are fixed, the values can be anything you need. You can have up to 11 data specifications for each test. `my/cool/project_test/should_test_multiple_spec.[1-9].edn` is a valid resource-location also.

Test can be executed by using `(clojure.test/run-tests)` and will produce the usual test-output like:

```console
Running namespace tests…
FAIL in: project_test.clj: 35: should-test-multiple-specs: project_test/should_test_multiple_specs.1.edn:
  expected: 1

  actual: (2)


4 tests finished, problems found. 😭 errors: 0, failures: 1, ns: 1, vars: 3
```

The test data resource used will determine the testing context.

As we are eating our own dog-food, we refactored our test and are using data-test. An example for refactoring you can find in our [dda-serverspec](https://github.com/DomainDrivenArchitecture/dda-serverspec-crate/commit/43abadbdb96afde6b1dc85834e465ee61eb464d2).

## Reference

### Loading Resources

data-test tries to locate data files on classpath as resource - should work similar in jar-files and in filesystem classpaths. The resource locations are determined by test namespace (e.g. `my/cool/project_test`) and defined test name (e.g. `should_test_multiple_spec.edn`). Resource-naming is following clojures usual filesystem mapping conventions.


```console
my/cool/project_test/should_test_multiple_spec.edn
my/cool/project_test/should_test_multiple_spec.1.edn
...
my/cool/project_test/should_test_multiple_spec.9.edn
```

A test is executed for every file defined.

### Bindings

`defdatatest name [binding-for-input binding-for-expected]` is translated in sth. like:

```clojure
(deftest should-test-with-data-explicit-version
  (let [testdata (loader/read-test-data-spec (io/resource "data_test_test/should_test_with_data_explicit_version.edn"))
        {:keys [input expected]} testdata]
    (is (= expected
           input))))
```

As you can see, input and expected can be used in your test code. You have to declare the used symbols in the binding section.

### Resources with AERO

You can use [aero](https://github.com/juxt/aero) in your test data declaration, e.g.

```clojure
{:references {:to-be-refernced "ref-test"}
 :input #ref [:references :to-be-refernced]
 :expected #ref [:references :to-be-refernced]}
```

### Adding Meta
You can add some metadata to your data files, e.g.

```clojure
{:input some-data
 :expected some-data
 :meta {:name "name of data"
        :description "describe what testcase is represented by this piec of data"
        :link "https://add-some-link-here"}}
```

### Generated Output

Each data-test execution will report it's results to "target/datatest/". The key `:test-event` reflect [clojure.test reporting events](https://github.com/clojure/clojure/blob/8c402a8c9695a4eddc07cbbe0d95d44e1372f0bf/src/clj/clojure/test.clj#L214), the key `:test-data-spec` reflects the used data-test data specification.

```clojure
(def TestDataReport
  {:test-event s/Any
   :test-data-spec {:input s/Any
                    :expected s/Any
                    :data-spec-file s/Str}})
```

## Development & mirrors
Development happens at: https://repo.prod.meissa.de/meissa/data-test

Mirrors are:
* https://gitlab.com/domaindrivenarchitecture/data-test (CI issues and PR)
* https://github.com/DomainDrivenArchitecture/data-test

## License

Copyright © 2022 meissa GmbH
Licensed under the [Apache License, Version 2.0](LICENSE) (the "License")
Pls. find licenses of our subcomponents [here](doc/SUBCOMPONENT_LICENSE)
