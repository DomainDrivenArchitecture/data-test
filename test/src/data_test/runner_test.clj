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
(ns data-test.runner-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [schema.core :as s]
   [data-test.runner :as sut]))

(deftest should-read-data
  (is (= {:simple "test"}
         (sut/read-data (io/resource "simple_aero.edn"))))
  (is (= {:to-be-refernced "ref-test", :key1 "ref-test", :key2 "ref-test"}
         (sut/read-data (io/resource "tagged_aero.edn"))))
  )

(deftest should-calculate-data-file-prefix
  (is (= "data_test/runner_test/test_it"
         (sut/data-file-prefix ::test-it))))

(deftest should-load-data
  (is (= {:test "data"}
         (sut/load-test-data (sut/data-file-prefix ::test-it)))))

(s/defmethod sut/data-test ::test-it
  [_ input :- s/Any expectation :- s/Any]
  "my-result")

(deftest should-reslove-multimethod
  (is (= "my-result"
         (sut/data-test (sut/create-test-runner ::test-it) nil nil))))
