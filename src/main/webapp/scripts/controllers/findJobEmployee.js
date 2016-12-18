angular.module('jobzz')
    .controller('FindJobEmployeeCtrl', ['$scope', '$rootScope', '$http', '$location', 'jobService', function ($scope, $rootScope, $http, $location, jobService) {
        var currentDate = new Date();
        $scope.job = {};

        $scope.minDate = new Date(
            currentDate.getFullYear(),
            currentDate.getMonth(),
            currentDate.getDate());

        $scope.maxDate = new Date(
            currentDate.getFullYear() + 10,
            currentDate.getMonth(),
            currentDate.getDate());

        $scope.job.startDate = $scope.minDate;
        $scope.job.endDate = $scope.maxDate;

        var getJobs = function () {
            var req = {
                method: 'GET',
                dataType: 'json',
                url: '/employee/all/available/jobs',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                }
            };

            $http(req).then(function (response) {

                $scope.jobs = response.data;

            }, function () {
                console.log('Fail to load the Jobs');
            });

        };

        getJobs();

        $scope.findJob = function () {

            var req = {
                method: 'POST',
                dataType: 'json',
                url: '/employee/find/available/jobs',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                data: $scope.job
            };

            $http(req).then(function (response) {

                $scope.jobs = response.data;

            }, function () {
                console.log('Fail to find Jobs');
            });

        };

        $scope.jobDetails = function (job) {

            jobService.setJob(job);
            $location.path('/employee/job/details').replace();

        };

    }]);
