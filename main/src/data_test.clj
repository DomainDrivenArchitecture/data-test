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
  (:require
   [clojure.test :as ct]
   [schema.core :as s]
   [data-test.runner :as runner]))

(s/defn test-with-data 
  [test-name :- s/Keyword]
  (runner/run-tests (runner/create-test-runner test-name)))

(defmacro defdatatest [name & body]
  '(do
    (clojure.test/deftest ~name
      ;TODO-1: bring macro to work
      ;TODO-2: crate filename out of package/namespace/test-name.edn
      ;TODO-3: enable more than one test-data-set with optional infix .##
      (let [testdata (data-test/read-data
                      (clojure.java.io/resource 
                       "data_test_test/should-test-with-data-macro-version.edn"))
            {:keys [input expectation]} testdata]
        ~@body))))
