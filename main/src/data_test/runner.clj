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
(ns data-test.runner
  (:require
   [schema.core :as s]
   [data-test.file-loader :as fl]))

(def TestResult
  {:input s/Any
   :expectation s/Any
   :output s/Any
   :passed? s/Bool
   :error s/Any})

(s/defrecord TestRunner [name :- s/Keyword]
  Object
  (toString [_] (str "TestRunner: " (:name _) "]")))

(defprotocol RunTest
  "Protocol for data driven tests"
  (name-prefix [dda-test])
  (run-tests [dda-test]))

(s/defn dispatch-by-name :- s/Keyword
  "Dispatcher for data-tests."
  [runner :- TestRunner
   input :- s/Any
   expectation :- s/Any]
  (:name runner))

(defmulti data-test
  "Multimethod for data-test."
  dispatch-by-name)

(extend-type TestRunner
  RunTest
  (name-prefix [_]
    (fl/data-test-spec-file-prefix (:name _)))
  (run-tests [_]
    (let [testdata (fl/load-test-data (fl/data-test-spec-file-prefix (:name _)))
          {:keys [input expectation]} testdata]
      (data-test _ input expectation))))

(defn create-test-runner 
  [test-name]
  (->TestRunner test-name))
