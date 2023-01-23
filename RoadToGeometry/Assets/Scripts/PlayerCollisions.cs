using System;
using System.Collections;
using System.Collections.Generic;
using Tasks;
using TMPro;
using UnityEngine;

public class PlayerCollisions : MonoBehaviour
{

    public AudioSource obstacleCollisionSound;
    public AudioSource correctObjectCollisionSound;
    public AudioSource wrongObjectCollisionSound;
    public GameObject taskManagerObject;
    public TextMeshProUGUI healthText;


    private int lives = 5;
    private const string CubeTag = "Cube";
    private const string CuboidTag= "Cuboid";
    private const string SphereTag = "Sphere";
    private const string CylinderTag = "Cylinder";
    private const string CapsuleTag = "Capsule";
    private readonly string[] _collectibleTags = {CubeTag, CuboidTag, SphereTag, CylinderTag, CapsuleTag};

    private void Start()
    {
        DisplayHealth();
    }

    private void OnTriggerEnter(Collider other)
    {
        foreach (var tagStr in _collectibleTags)
        {
            if (other.CompareTag(tagStr))
            {
                TaskManager taskManager = taskManagerObject.GetComponent<TaskManager>();
                bool isObjectFromTheTask = taskManager.CurrentTask.IsObjectFromTheTask(other.gameObject);
                if (isObjectFromTheTask && PlayerPrefs.GetInt("SoundEffectsToggle") == 0) correctObjectCollisionSound.Play();
                else if (!isObjectFromTheTask)
                {
                    if (PlayerPrefs.GetInt("SoundEffectsToggle") == 0) wrongObjectCollisionSound.Play();
                    lives -= 1;
                }

                EventManager.Instance.ObjectCollected(other.gameObject);
                Destroy(other.gameObject);
            }
        }
        DisplayHealth();
        CheckIfAnyLivesLeft();
    }

    private void OnCollisionEnter(Collision collision)
    {
        transform.GetComponent<GameOver>().EndGame();
        if (PlayerPrefs.GetInt("SoundEffectsToggle") == 0)
        {
            obstacleCollisionSound.Play();
        }
    }

    private void DisplayHealth()
    {
        string health = "";
        for (int i = 0; i < lives; i++)
        {
            health += "♥";
        }
        healthText.text = health;
    }

    private void CheckIfAnyLivesLeft()
    {
        if (lives <= 0)
        {
            transform.GetComponent<GameOver>().EndGame();
        }
    }
}
