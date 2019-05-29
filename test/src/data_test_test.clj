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
(ns data-test-test
  (:require
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [data-test.loader :as loader]
   [data-test :as sut]))

; -------------------- explicit version ------------------
(deftest should-test-with-data-explicit-version
  (let [testdata (loader/read-test-data-spec (io/resource "data_test_test/should-test-with-data-explicit-version.edn"))
        {:keys [input expected]} testdata]
    (is (= expected
           input))))

; ---------------------------- macro -----------------------------
(sut/defdatatest should-test-with-data-macro-version [input expected]
    (is (= input expected)))

(sut/defdatatest should-test-multiple-specs [input expected]
  (is (= input expected)))
