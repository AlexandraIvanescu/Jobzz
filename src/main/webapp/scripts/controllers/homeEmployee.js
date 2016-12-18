angular.module('jobzz')
    .controller('HomeEmployeeCtrl', ['$scope', '$rootScope', '$http', '$location', 'jobService',
        function ($scope, $rootScope, $http, $location, jobService) {

            var getJobs = function () {
                var req = {
                    method: 'GET',
                    dataType: 'json',
                    url: '/employee/all/post',
                    headers: {
                        'Content-Type': 'application/json; charset=utf-8'
                    }
                };

                $http(req).then(function (response) {
                    $scope.jobsWaiting = response.data.jobsWaiting;
                    $scope.jobsProgress = response.data.jobsProgress;
                    $scope.jobsDone = response.data.jobsDone;
                }, function () {
                    console.log('fail');
                });

            };

            getJobs();

            $scope.postDetails = function (job) {
                jobService.setJob(job);
                $location.path('/employee/post/details').replace();
            }

        }
    ]);