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
   [clojure.java.io :as io]
   [clojure.test :as ct]
   [schema.core :as s]
   [aero.core :as aero]))

;TODO: replace schema with spec
(def TestDataSet
  {:input s/Any
   :expectation s/Any})

(defn read-data
  [resource-url]
  (aero/read-config resource-url))

(defmacro defdatatest [name & body]
  `(do
      (ct/deftest ~name 
        (let [testdata (sut/read-data (io/resource "should-test-with-data-macro-version.edn"))
              {:keys [input expectation]} testdata]
          ~body))))
