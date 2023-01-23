using System;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

namespace Tasks
{
    public class TaskManager : MonoBehaviour
    {
        public TextMeshProUGUI taskText, scoreText, gameOverScoreText;
        public GameObject player;
        public List<GameObject> objectPrefabs;
        public List<string> cubeHints;
        public List<string> cuboidHints;
        public List<string> sphereHints;
        public List<string> cylinderHints;
        public List<string> capsuleHints;
        private Dictionary<string, List<string>> _collectiblesHints = new();
        private static System.Random _random = new System.Random();
        public Task CurrentTask { get; set; }

        private const string CubeTag = "Cube";
        private const string CuboidTag = "Cuboid";
        private const string SphereTag = "Sphere";
        private const string CylinderTag = "Cylinder";
        private const string CapsuleTag = "Capsule";

        private void Start()
        {
            InitCollectiblesHints();
            NewCurrentTask();
            EventManager.Instance.TaskCompletedEvent += OnTaskCompleted;
            EventManager.Instance.ObjectCollectedEvent += OnObjectCollected;
            EventManager.Instance.GameOverEvent += OnGameOver;
        }

        private void InitCollectiblesHints()
        {
            _collectiblesHints.Add(CubeTag, cubeHints);
            _collectiblesHints.Add(CuboidTag, cuboidHints);
            _collectiblesHints.Add(SphereTag, sphereHints);
            _collectiblesHints.Add(CylinderTag, cylinderHints);
            _collectiblesHints.Add(CapsuleTag, capsuleHints);
        }

        private void DisplayTask(Task task)
        {
            taskText.text = string.Join(", ", task.TaskStrings());
        }

        private Task CreateNewTask()
        {
            return new Task(objectPrefabs, _collectiblesHints);
        }

        private void NewCurrentTask()
        {
            var task = CreateNewTask();
            CurrentTask = task;
            DisplayTask(task);
        }

        private void OnTaskCompleted(Task task)
        {
            AddPoints(task.Points);
            NewCurrentTask();
        }

        private void AddPoints(int points)
        {
            var score = int.Parse(scoreText.text);
            scoreText.text = score + points + "";
        }

        private void OnObjectCollected(GameObject go)
        {
            CurrentTask.Collect(go);
            DisplayTask(CurrentTask); //number of objects left to collect changes
        }

        private void OnGameOver()
        {
            gameOverScoreText.text = scoreText.text;
        }
    }
}