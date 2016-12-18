angular.module('jobzz')
    .controller('PayEmployeePostEmployerCtrl', ['$scope', '$rootScope', '$http', 'payEmployeePostService', function ($scope, $rootScope, $http, payEmployeePostService) {
        $scope.review = {};
        $scope.review.point = 5;
        $scope.employeePost = payEmployeePostService.getPost();

        $scope.closeDialog = function () {
            $rootScope.panelRef && $rootScope.panelRef.close().then(function () {
                angular.element(document.querySelector('.dialog-button')).focus();
                $rootScope.panelRef.destroy();
            });
        };

        $scope.payEmployee = function () {
            $scope.review.employee = $scope.employeePost.employee;

            var req = {
                method: 'POST',
                dataType: 'json',
                url: '/employer/pay/employee/post',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                data: {review: $scope.review, employeePost: $scope.employeePost}
            };

            $http(req).then(function (response) {

                if (response.data.isPaid) {
                    payEmployeePostService.setPostIsPaid(true);
                    $scope.closeDialog();
                }

            }, function () {
                console.log('Fail to delete the post !');
            });

        }

    }]);