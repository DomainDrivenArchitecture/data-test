; Licensed to the Apache Software Foundation (ASF) under one
; or more contributor license agreements. See the NOTICE file
; distributed with this work for additional information
; regarding copyright ownership. The ASF licenses this file
; to you under the Apache License, Version 2.0 (the
; "License"); you may not use this file except in compliance
; with the License. You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
(ns data-test
  ^{:author "Michael Jerger, with contributions and suggestions by razum2um",
    :doc "data-test separates test data from test code and allows a 
more data driven approach for testing. In case of having huge amounts 
of test-input & -expectations your test code will remain readable and concise. 
data-test is founded on and compatible with `clojure.test`. Integration in your 
test environments will work without any changes. For explicit, intentful and 
obvious data, data-test uses aero (see: https://github.com/juxt/aero).

USAGE
Define your data test similar to deftest and express your test e.g. with 
is macro. The given binding [input expected] will let symbols which can be 
used in your test code.

Example:

(ns my.cool.project-test
  (:require [clojure.test :refer :all]
            [data-test :refer :all]))

(defdatatest should-test-multiple-specs [input expected]
  (is (= input expected)))

Your test data are loaded as resource and therefore data has to be located in 
classpath. The path is determined by your tests namespace and your tests 
definition name. In given case it is 

my/cool/project_test/should_test_multiple_spec.edn

{:input 1
 :expected 1}

The keys :input and :expected are fixed, the values can be anything you need. 
You can have up to 11 data specifications for each test. 

my/cool/project_test/should_test_multiple_spec.[1-9].edn

is a valid resource-location also.

Test can be executed by using 

(clojure.test/run-tests)

 and will produce the usual test-output like:

Running namespace tests…
FAIL in: project_test.clj: 35: should-test-multiple-specs: project_test/should_test_multiple_specs.1.edn:
  expected: 1

  actual: (2)


4 tests finished, problems found. 😭 errors: 0, failures: 1, ns: 1, vars: 3

The test data resource used will determine the testing context."}
  (:require
   [clojure.test :as t]
   [data-test.reporter :as reporter]
   [data-test.loader :as loader]))

(def TestDataSpec loader/TestDataSpec)

(defmacro defdatatest [n bindings & body]
  (when t/*load-tests*
    (let [namespaced-test-key# (keyword (str *ns*) (name n))]
      `(def ~(vary-meta n assoc
                        :test `(fn [] 
                                 (doseq [data-spec# (loader/load-data-test-specs ~namespaced-test-key#)]
                                  (let [~(symbol (first bindings)) (:input data-spec#)
                                        ~(symbol (second bindings)) (:expected data-spec#)
                                        data-spec-file# (:data-spec-file data-spec#)
                                        message# (new java.io.StringWriter)]
                                    (binding [t/*testing-contexts*
                                              (conj t/*testing-contexts* data-spec-file#)
                                              reporter/*data-test-report-context* data-spec#]
                                      ~@body)
                                    )))
                        :data-spec-key namespaced-test-key#)
           (fn [] (t/test-var (var ~n)))))))
