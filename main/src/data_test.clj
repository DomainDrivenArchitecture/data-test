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
                                        ~(symbol (second bindings)) (:expectation data-spec#)
                                        data-spec-file# (:data-spec-file data-spec#)
                                        message# (new java.io.StringWriter)]
                                    (binding [t/*testing-contexts*
                                              (conj t/*testing-contexts* data-spec-file#)
                                              reporter/*data-test-report-context* data-spec#]
                                      ~@body)
                                    )))
                        :data-spec-key namespaced-test-key#)
           (fn [] (t/test-var (var ~n)))))))
