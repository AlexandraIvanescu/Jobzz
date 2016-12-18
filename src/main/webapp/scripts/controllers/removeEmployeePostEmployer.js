angular.module('jobzz')
    .controller('RemoveEmployeePostEmployerCtrl', ['$scope', '$rootScope', '$http', 'removeEmployeePostService', function ($scope, $rootScope, $http, removeEmployeePostService) {
        $scope.review = {};
        $scope.review.point = 5;
        $scope.employeePost = removeEmployeePostService.getPost();

        $scope.closeDialog = function () {
            $rootScope.panelRef && $rootScope.panelRef.close().then(function () {
                angular.element(document.querySelector('.dialog-button')).focus();
                $rootScope.panelRef.destroy();
            });
        };

        $scope.addReview = function () {
            $scope.review.employee = $scope.employeePost.employee;

            var req = {
                method: 'POST',
                dataType: 'json',
                url: '/employer/remove/employee/post',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                data: {review: $scope.review, employeePost: $scope.employeePost}
            };

            $http(req).then(function (response) {

                if (response.data.isRemoved) {
                    removeEmployeePostService.setPostIsDeleted(true);
                    $scope.closeDialog();
                }

            }, function () {
                console.log('Fail to delete the post !');
            });

        }

    }]);