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
(ns data-test.file-loader
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [schema.core :as s]
   [aero.core :as aero]))

;TODO: replace schema with spec
(def TestDataSpec
  {:input s/Any
   :expectation s/Any})

(s/defn read-test-data-spec :- TestDataSpec
  [resource-url :- s/Str]
  (aero/read-config resource-url))

(s/defn data-test-spec-file-prefix :- s/Str
  [name-key :- s/Keyword]
  (str/replace
    (str/replace (str (namespace name-key) "/" (name name-key))
                  #"-" "_")
    #"\." "/"))

(s/defn data-test-spec-file-names :- [s/Str]
  [name-key :- s/Keyword]
  (let [prefix (data-test-spec-file-prefix name-key)]
    (cons (str prefix ".edn")
          (map #(str prefix "." % ".edn")
                (range 10)))))

(s/defn load-test-data
  [file-prefix :- s/Str]
  (let [file-path (str file-prefix ".edn")]
    (try
      (read-test-data-spec (io/resource file-path))
      (catch IllegalArgumentException e
             (throw (ex-info (str "Could not find test spec on " file-path)
                             {:message "Could not find test spec"
                              :file-path file-prefix} e))))))
