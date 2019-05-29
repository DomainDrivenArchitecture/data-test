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
(ns data-test.reporter
  (:require
   [clojure.test :as t]
   [clojure.java.io :as io]
   [clojure.stacktrace :as stack]
   [schema.core :as s]))

(def ^:dynamic 
  *data-test-report-context* nil)

(s/defn write-data-test-output
  [m]
  (let [data-spec-file (:data-spec-file *data-test-report-context*)
        output-file (str "target/datatest/" data-spec-file)]
    (io/make-parents output-file)
    (spit output-file (merge
                       {:test-event m}
                       *data-test-report-context*))))

(defmethod t/report :default [m]
  (t/with-test-out (prn m)))

(defmethod t/report :pass [m]
  (when *data-test-report-context* (write-data-test-output m))
  (t/with-test-out (t/inc-report-counter :pass)))

(defmethod t/report :fail [m]
  (t/with-test-out
    (t/inc-report-counter :fail)
    (println "\nFAIL in" (t/testing-vars-str m))
    (when (seq t/*testing-contexts*) (println (t/testing-contexts-str)))
    (when-let [message (:message m)] (println message))
    (println "expected:" (pr-str (:expected m)))
    (println "  actual:" (pr-str (:actual m)))))

(defmethod t/report :error [m]
  (t/with-test-out
    (t/inc-report-counter :error)
    (println "\nERROR in" (t/testing-vars-str m))
    (when (seq t/*testing-contexts*) (println (t/testing-contexts-str)))
    (when-let [message (:message m)] (println message))
    (println "expected:" (pr-str (:expected m)))
    (print "  actual: ")
    (let [actual (:actual m)]
      (if (instance? Throwable actual)
        (stack/print-cause-trace actual t/*stack-trace-depth*)
        (prn actual)))))