# data-test
[![Clojars Project](https://img.shields.io/clojars/v/dda/data-test.svg)](https://clojars.org/dda/data-test)
[![Build Status](https://travis-ci.org/DomainDrivenArchitecture/data-test.svg?branch=master)](https://travis-ci.org/DomainDrivenArchitecture/data-test)

[![Slack](https://img.shields.io/badge/chat-clojurians-green.svg?style=flat)](https://clojurians.slack.com/messages/#dda-pallet/) | [<img src="https://domaindrivenarchitecture.org/img/meetup.svg" width=50 alt="DevOps Hacking with Clojure Meetup"> DevOps Hacking with Clojure](https://www.meetup.com/de-DE/preview/dda-pallet-DevOps-Hacking-with-Clojure) | [Website & Blog](https://domaindrivenarchitecture.org)

## About

data-test separates test data from test code and allows a more data driven approach for testing. In case of having huge amounts of test-input & -expectations your test code will remain readable and concise. data-test is founded on and compatible with `clojure.test`. Integration in your test environments will work. For explicit, intentful and obvious data, data-test uses (https://github.com/juxt/aero)[aero].

## Usage

Define your data test similar to `deftest` and express your test e.g. with `is` macro. The given binding `[input expected]` can be used in your testcode.

```clojure
(ns my.cool.project-test
  (:require [clojure.test :refer :all]
            [data-test :refer :all])

(defdatatest should-test-multiple-specs [input expected]
  (is (= input expected)))
```

Your test data has to be located in classpath. The path is determined by your tests namespace and your tests definition name. In given case it is `my/cool/project_test/should_test_multiple_spec.edn`:

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

The test data file used will determine the testing context.

## Reference

### Loading Resources
### Bindings
### Resources with AERO
### Generated Output

## License

Copyright © 2019 meissa GmbH
Licensed under the [Apache License, Version 2.0](LICENSE) (the "License")
Pls. find licenses of our subcomponents [here](doc/SUBCOMPONENT_LICENSE)
