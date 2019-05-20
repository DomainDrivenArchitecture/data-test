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
   [clojure.java.io :as io]
   [clojure.string :as str]
   [schema.core :as s]
   [aero.core :as aero]))

;TODO: replace schema with spec
(def TestDataSet
  {:input s/Any
   :expectation s/Any})

(s/defrecord TestRunner [name :- s/Keyword]
  Object
  (toString [_] (str "TestRunner: " (:name _) "]")))

(defprotocol RunTest
  "Protocol for data driven tests"
  (name-prefix [dda-test])
  (run-test [dda-test])
  )

(s/defn dispatch-by-name :- s/Keyword
  "Dispatcher for data-tests."
  [runner :- TestRunner
   input :- s/Any
   expectation :- s/Any]
  (:name runner))

(defmulti data-test
  "Multimethod for data-test."
  dispatch-by-name)

(s/defn read-data :- TestDataSet
  [resource-url :- s/Str]
  (aero/read-config resource-url))

(s/defn data-file-prefix :- s/Str
  [name-key :- s/Keyword]
  (str/replace
    (str/replace (str (namespace name-key) "/" (name name-key))
                  #"-" "_")
    #"\." "/"))

(s/defn load-test-data
  [file-prefix :- s/Str]
  (let [file-path (str file-prefix ".edn")]
    (println file-path)
    (read-data
     (io/resource file-path))))

(extend-type TestRunner
  RunTest
  (name-prefix [_]
    (data-file-prefix (:name _)))
  (run-test [_]
    (let [testdata (load-test-data (data-file-prefix (:name _)))
          {:keys [input expectation]} testdata]
      (data-test _ input expectation))))

(defn create-test-runner 
  [test-name]
  (->TestRunner test-name))
